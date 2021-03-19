package com.lindenlabs.scorebook.androidApp.base.domain

import com.lindenlabs.scorebook.androidApp.base.data.sources.GameDataSource
import com.lindenlabs.scorebook.androidApp.base.utils.DefaultDispatcherProvider
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.settings.UserSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class AppRepository(
    val dataSource: GameDataSource,
    private val settings: UserSettings,
    val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default(),
) : GameDataSource, UserSettings by settings {

    override suspend fun load(): List<Game> =
        withContext(dispatcher) {
            dataSource.load()
        }

    override suspend fun getGameById(id: UUID): Game? = withContext(dispatcher) {
        dataSource.getGameById(id)
    }

    override suspend fun storeGame(game: Game) = withContext(dispatcher) {
        dataSource.storeGame(game)
    }

    override suspend fun storeGame(index: Int, game: Game) = withContext(dispatcher) {
        dataSource.storeGame(index, game)
    }

    override suspend fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int) =
        withContext(dispatcher) {
            dataSource.roundPlayed(game, lastPlayer, newScore)
        }

    override suspend fun updateGame(game: Game) = withContext(dispatcher) {
        dataSource.updateGame(game)
    }

    override suspend fun deleteGame(game: Game) = withContext(dispatcher) {
        dataSource.deleteGame(game)
    }

    override suspend fun clear() = withContext(dispatcher) {
        dataSource.clear()
    }
}