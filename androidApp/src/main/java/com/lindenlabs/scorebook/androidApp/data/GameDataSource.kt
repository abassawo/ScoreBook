package com.lindenlabs.scorebook.androidApp.data

import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

typealias PairOfOpenToClosedGames = Pair<List<Game>, List<Game>>

interface GameDataSource {
    fun load(callback: (PairOfOpenToClosedGames) -> Unit)
    fun getGameById(id: UUID) : Game?
    fun storeGame(game: Game)
    fun updateGame(game: Game, lastPlayer: Player, newScore: Int)
    fun updatePlayers(game: Game, players: List<Player>) : List<Player>
    fun clear()
}

interface PlayerDataSource {
    val players: List<Player>
    fun addPlayer(player: Player)
}