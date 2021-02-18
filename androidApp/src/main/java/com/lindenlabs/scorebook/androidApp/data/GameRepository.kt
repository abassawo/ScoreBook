package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

object GameRepository : GameDataSource {
    override val games: MutableList<Game> = mutableListOf()
    private val mapOfGameToPlayers = mutableMapOf<Game, MutableList<Player>>()

    override fun getGameById(id: UUID): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) {
        games += game
        mapOfGameToPlayers[game] = mutableListOf()
    }

    override fun addPlayers(game: Game, players: List<Player>): List<Player> {
        mapOfGameToPlayers[game] = (getPlayers(game) + players).toMutableList()
        return getPlayers(game)
    }


    override fun getPlayers(game: Game): MutableList<Player> {
        return mapOfGameToPlayers[game] ?: mutableListOf()
    }
}
