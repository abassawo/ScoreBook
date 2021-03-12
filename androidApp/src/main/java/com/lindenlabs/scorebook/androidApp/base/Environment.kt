package com.lindenlabs.scorebook.androidApp.base

import com.lindenlabs.scorebook.androidApp.base.data.DefaultDispatcherProvider
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.util.*


class Environment(val gamesRepo: GameDataSource,
                  val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default(),
                  val coroutineScope: CoroutineScope? = null
) : GameDataSource  {

    override suspend fun load(): List<Game> =
        withContext(dispatcher) {
            gamesRepo.load()
        }

    override suspend fun getGameById(id: UUID): Game? = withContext(dispatcher) {
        gamesRepo.getGameById(id)
    }

    override suspend fun storeGame(game: Game) = withContext(dispatcher) {
        gamesRepo.storeGame(game)
    }

    override suspend fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int) = withContext(dispatcher) {
        gamesRepo.roundPlayed(game, lastPlayer, newScore)
    }

    override suspend fun updateGame(game: Game)  = withContext(dispatcher) {
        gamesRepo.updateGame(game)
    }

    override suspend fun clear() = withContext(dispatcher) {
        gamesRepo.clear()
    }
}