package com.lindenlabs.scorebook.androidApp.base.domain

import android.app.Application
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GameStore
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GameStoreServiceHandler
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GamesDatabase
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import java.util.*
import javax.inject.Inject

class PersistentGameRepository @Inject constructor(gamesDao: GameStore): GameDataSource {
    private val gamesService: GameStoreServiceHandler = GameStoreServiceHandler (gamesDao)
    var games: List<Game> = emptyList()

    override fun load(callback: (PairOfOpenToClosedGames) -> Unit) {
        gamesService.getAllGames { games ->
            this.games = games
            val openGames = GetOpenGames(games).invoke()
            val closedGames = GetClosedGames(games).invoke()
            callback(openGames to closedGames)
        }
    }

    override fun getGameById(id: UUID): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) = gamesService.addGame(game)

    override fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int) =
        gamesService.updateGame(game)

    override fun updateGame(game: Game) = gamesService.updateGame(game)

    override fun clear() = gamesService.removeGames(*games.toTypedArray())

    companion object {
        @Volatile private var INSTANCE: PersistentGameRepository? = null

        fun getInstance(gamesDao: GameStore): PersistentGameRepository =
            INSTANCE ?: PersistentGameRepository(gamesDao)
    }
}