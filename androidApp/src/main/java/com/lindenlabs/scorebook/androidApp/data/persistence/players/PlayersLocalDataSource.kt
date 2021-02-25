package com.lindenlabs.scorebook.androidApp.data.persistence.players

import android.os.Handler
import android.os.Looper
import com.lindenlabs.scorebook.androidApp.data.model.Player
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
class PlayersLocalDataSource(private val playerStore: PlayerStore) {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    operator fun invoke(action: () -> Unit) = executorService.execute { action() }

    fun addPlayer(player: Player) = invoke { playerStore.insertAll(player) }

    fun updatePlayer(player: Player) = invoke { playerStore.updatePlayer(player) }

    fun getAllPlayers(callback: (List<Player>) -> Unit) {
        executorService.execute {
            val players = playerStore.getAll()
            mainThreadHandler.post { callback(players) }
        }
    }

    fun removePlayers() {
        executorService.execute {
            playerStore.clearTable()
        }
    }

    fun addPlayers(players: MutableList<Player>) {
        for (player in players)
            playerStore.insertAll(player)
    }
}