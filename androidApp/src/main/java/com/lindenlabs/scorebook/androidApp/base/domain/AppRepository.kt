package com.lindenlabs.scorebook.androidApp.base.domain

import com.lindenlabs.scorebook.androidApp.base.data.sources.GameDataSource
import com.lindenlabs.scorebook.androidApp.base.utils.DefaultDispatcherProvider
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.data.sources.PlayerDataSource
import com.lindenlabs.scorebook.androidApp.settings.UserSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class AppRepository(
    private val gameDataSource: GameDataSource,
    private val playersDataSource: PlayerDataSource,
    private val settings: UserSettings,
    val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default(),
) : UserSettings by settings {


    suspend fun load(): List<Game> =
        withContext(dispatcher) {
            gameDataSource.load()
        }

   suspend fun getGameById(id: UUID): Game? = withContext(dispatcher) {
        gameDataSource.get(id)
    }

   suspend fun storeGame(game: Game) = withContext(dispatcher) {
        gameDataSource.store(game)
        val newPlayers = game.players
        val playersInDB = getPlayers()
       for(player in game.players) {
           if(!playersInDB.contains(player)) {
               addPlayer(player)
           }
       }
    }

    suspend fun updateGame(game: Game) = withContext(dispatcher) {
        gameDataSource.update(game)
    }

   suspend fun deleteGame(game: Game) = withContext(dispatcher) {
        gameDataSource.delete(game)
    }

   suspend fun clearGames() = withContext(dispatcher) {
        gameDataSource.clear()
    }

    suspend fun getPlayers() = withContext(dispatcher) {
        playersDataSource.load()
    }

    suspend fun addPlayer(player: Player)  = withContext(dispatcher) {
        if(playersDataSource.get(player.id) == null) {
            playersDataSource.store(player)
        }
    }
}