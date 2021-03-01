package com.lindenlabs.scorebook.androidApp.data

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.lindenlabs.scorebook.androidApp.data.persistence.GameStore
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import java.util.*

class PersistentGameRepository(
    private val gamesDatabase: GameDatabaseHandler
) : GameDataSource {

    var games: List<Game> = emptyList()

    override fun load(callback: (PairOfOpenToClosedGames) -> Unit)  {
        gamesDatabase.getAllGames { games ->
            this.games = games
        }

        val openGames = GetOpenGames(games).invoke()
        val closedGames = GetClosedGames(games).invoke()
        callback(openGames to closedGames)
    }

    override fun getGameById(id: UUID): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) {
        gamesDatabase.addGame(game)
    }

    override fun updateGame(game: Game, lastPlayer: Player, newScore: Int) {
        gamesDatabase.updateGame(game)
    }

    override fun updatePlayers(game: Game, player: List<Player>): List<Player> {
        gamesDatabase.updateGame(game)
        return game.players
    }

    override fun clear() = gamesDatabase.removeGames(*games.toTypedArray())
}

class GameDatabaseHandler(private val gameStore: GameStore) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)

    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private operator fun invoke(action: () -> Unit) = executorService.execute { action() }

    fun addGame(game: Game) = invoke { gameStore.insert(game) }


    fun getAllGames(callback: (List<Game>) -> Unit) = invoke {
        val games = gameStore.loadAll()
        mainThreadHandler.post { callback(games) }
    }

    fun removeGames(vararg games: Game) = invoke {
        gameStore.delete(*games)
    }

    fun addGames(games: MutableList<Game>) = invoke {
        for (game in games)
            gameStore.insert(game)
    }

    fun updateGame(game: Game) = invoke {
        gameStore.update(game)
    }
}