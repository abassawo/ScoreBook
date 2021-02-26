package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

interface GameDataSource {
    val games : List<Game>
    fun getAllGames(action: (games: List<Game>) -> Unit)
    fun getGameById(id: Long) : Game?
    fun storeGame(game: Game)
    fun updateGame(game: Game, lastPlayer: Player, newScore: Int)
    fun updatePlayers(game: Game, player: List<Player>) : List<Player>
    fun addPlayer(game: Game, player: Player): List<Player>
    fun endGame(game: Game)
}