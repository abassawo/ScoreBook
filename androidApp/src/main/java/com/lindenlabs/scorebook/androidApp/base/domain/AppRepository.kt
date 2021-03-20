package com.lindenlabs.scorebook.androidApp.base.domain

import com.lindenlabs.scorebook.androidApp.base.data.sources.GameDataSource
import com.lindenlabs.scorebook.androidApp.base.utils.DefaultDispatcherProvider
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.settings.UserSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class AppRepository(
    private val gameDataSource: GameDataSource,
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
}