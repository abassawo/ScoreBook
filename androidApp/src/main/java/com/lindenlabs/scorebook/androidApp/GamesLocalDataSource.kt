package com.lindenlabs.scorebook.androidApp

import android.os.Handler
import android.os.Looper
import com.lindenlabs.scorebook.androidApp.data.persistence.GameDao
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
class GamesLocalDataSource(private val gameDao: GameDao) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    operator fun invoke(action: () -> Unit) = executorService.execute { action() }

    fun addGame(game: Game) = invoke { gameDao.insertAll(game) }

    fun getAllGames(callback: (List<Game>) -> Unit) {
        executorService.execute {
            val games = gameDao.getAll()
            mainThreadHandler.post { callback(games) }
        }
    }

    fun removeGames() {
        executorService.execute {
            gameDao.clearTable()
        }
    }
}
