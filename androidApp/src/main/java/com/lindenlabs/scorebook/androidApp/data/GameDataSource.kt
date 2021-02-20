package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

interface GameDataSource {
    val games : List<Game>
    fun getGameById(id: UUID) : Game?
    fun storeGame(game: Game)
    fun updateGame(game: Game, lastPlayer: Player, newScore: Int)
    fun addPlayers(game: Game, player: List<Player>) : List<Player>
    fun getPlayers(game: Game) : List<Player>
}