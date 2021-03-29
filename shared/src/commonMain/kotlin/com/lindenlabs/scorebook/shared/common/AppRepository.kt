package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

class AppRepository(
    private val gameDataSource: DataSource<Game>,
    private val playersDataSource: DataSource<Player>,
    private val settings: UserSettings
) : UserSettings by settings {


    suspend fun load(): List<Game> =
        gameDataSource.load()


    suspend fun storeGame(game: Game) {
//        withContext(dispatcher) {
        gameDataSource.store(game)
//            game.players.forEach { player ->
//                addPlayer(player)
//            }
//        }
    }

    suspend fun updateGame(game: Game) {
//        withContext(dispatcher) {
        gameDataSource.update(game)
//        }
    }

    suspend fun deleteGame(game: Game) {
//        withContext(dispatcher) {
        gameDataSource.delete(game)
//        }
    }

    suspend fun getPlayers(): List<Player> =
        playersDataSource.load()


    suspend fun addPlayer(player: Player) {
        val playersInDB = getPlayers()
        if (playersInDB.find { it.name == player.name } == null) {
            playersDataSource.store(player)
        }
    }
}