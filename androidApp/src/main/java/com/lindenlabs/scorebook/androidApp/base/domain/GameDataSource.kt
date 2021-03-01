package com.lindenlabs.scorebook.androidApp.base.domain

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import java.util.*

typealias PairOfOpenToClosedGames = Pair<List<Game>, List<Game>>

interface GameDataSource {
    fun load(callback: (PairOfOpenToClosedGames) -> Unit)
    fun getGameById(id: UUID) : Game?
    fun storeGame(game: Game)
    fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int)
    fun updateGame(game: Game)
    fun clear()
}
