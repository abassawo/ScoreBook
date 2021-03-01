package com.lindenlabs.scorebook.androidApp.base.data.persistence

import android.os.Handler
import android.os.Looper
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GameStoreServiceHandler(private val gameStore: GameStore) {

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

    fun updateGame(game: Game) = invoke {
        gameStore.update(game)
    }
}