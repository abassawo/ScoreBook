package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.DefaultDispatcherProvider
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppRepository(
    private val gameDataSource: DataSource<Game>,
    private val playersDataSource: DataSource<Player>,
    val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default(),
)  {

    suspend fun load(): List<Game> = withContext(dispatcher) {
        gameDataSource.load()
    }

    suspend fun storeGame(game: Game) =
        withContext(dispatcher) {
            gameDataSource.store(game)
            game.players.forEach { player ->
                addPlayer(player)
            }
        }

    suspend fun updateGame(game: Game) = withContext(dispatcher) {
        gameDataSource.update(game)
    }

    suspend fun deleteGame(game: Game) = withContext(dispatcher) {
        gameDataSource.delete(game)
    }

    suspend fun getPlayers(): List<Player> = withContext(dispatcher) {
        playersDataSource.load()
    }


    suspend fun addPlayer(player: Player) = withContext(dispatcher) {
        val playersInDB = getPlayers()

        if (playersInDB.find { it.name == player.name } == null) {
            playersDataSource.store(player)
        }
    }

    suspend fun getGame(gameId: String): Game {
        return gameDataSource.get(gameId)
    }

    suspend fun getPlayer(playerId: String): Player = playersDataSource.get(playerId)
}