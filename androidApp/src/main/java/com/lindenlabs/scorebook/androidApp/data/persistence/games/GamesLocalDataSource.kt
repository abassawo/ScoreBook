package com.lindenlabs.scorebook.androidApp.data.persistence.games

import android.os.Handler
import android.os.Looper
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
class GamesLocalDataSource(private val gameStore: GameStore) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    operator fun invoke(action: () -> Unit) = executorService.execute { action() }

    fun addGame(game: GameModelRaw) = invoke { gameStore.insertAll(game) }

    fun updateGame(game: GameModelRaw) = invoke { gameStore.updateGames(game) }

    fun getAllGames(callback: (List<GameModelRaw>) -> Unit) = invoke {
        val games = gameStore.getAll()
        mainThreadHandler.post { callback(games) }
    }

    fun removeGames() = invoke {
        gameStore.clearTable()
    }

    fun addGames(games: MutableList<Game>) = invoke{
        for (game in games)
            gameStore.insertAll(GameModelRaw(game.name, game.isClosed))
    }
}
