package com.lindenlabs.scorebook.androidApp.base.data.sources

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import java.util.*

interface GameDataSource {
    suspend fun load(): List<Game>
    suspend fun getGameById(id: UUID) : Game?
    suspend fun storeGame(game: Game)
    suspend fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int)
    suspend fun updateGame(game: Game)
    suspend fun clear()
}
