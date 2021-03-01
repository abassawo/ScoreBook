package com.lindenlabs.scorebook.androidApp.data

import android.os.Handler
import android.os.Looper
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.lindenlabs.scorebook.androidApp.data.persistence.GameStore
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

class PersistentGameRepository(
    private val gameRepository: GameRepository = GameRepository(),
    private val gamesDatabase: GameDatabaseHandler
) : GameDataSource {

    init {
        gameRepository.clear()
        gamesDatabase.getAllGames { games ->
            games.forEach {it -> gameRepository.games += it }
        }
    }

    override val games: List<Game> = gameRepository.games

    override fun getGameById(id: UUID): Game? = gameRepository.getGameById(id)

    override fun storeGame(game: Game) {
        gameRepository.storeGame(game)
        gamesDatabase.addGame(game)
    }

    override fun updateGame(game: Game, lastPlayer: Player, newScore: Int) =
        gameRepository.updateGame(game, lastPlayer, newScore)

    override fun updatePlayers(game: Game, player: List<Player>): List<Player> {
        gameRepository.updatePlayers(game, player)
        gamesDatabase.updateGame(game)
        return game.players
    }

    override fun clear() {
        gameRepository.clear()
        // todo  - remove from database as well
    }
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