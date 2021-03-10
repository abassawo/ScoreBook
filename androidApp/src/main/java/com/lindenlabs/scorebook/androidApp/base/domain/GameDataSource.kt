package com.lindenlabs.scorebook.androidApp.base.domain

import androidx.lifecycle.LiveData
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import java.util.*

typealias PairOfOpenToClosedGames = Pair<List<Game>, List<Game>>

interface GameDataSource {
//    suspend fun load(callback: (PairOfOpenToClosedGames) -> Unit)
    suspend fun load(): List<Game>
    suspend fun getGameById(id: UUID) : Game?
    suspend fun storeGame(game: Game)
    suspend fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int)
    suspend fun updateGame(game: Game)
    suspend fun clear()
}
