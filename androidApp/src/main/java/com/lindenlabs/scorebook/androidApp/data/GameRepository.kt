package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Round
import java.lang.IllegalStateException
import java.util.*

class GameRepository : GameDataSource {
    override val games: MutableList<Game> = mutableListOf()

    override fun getGameById(id: UUID): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) {
        games += game
    }

    override fun updateGame(game: Game, lastPlayer: Player, addedScore: Int) {
        if(!game.players.contains(lastPlayer)) throw IllegalStateException()
        else {
            lastPlayer.scoreTotal += addedScore
            lastPlayer.rounds += Round(score = lastPlayer.scoreTotal)
        }
    }

    override fun updatePlayers(game: Game, players: List<Player>) : List<Player> {
        game.players = players
        games[games.indexOf(game)] = game
        return game.players
    }

    override fun addPlayer(game: Game, player: Player): List<Player> =
        updatePlayers(game, game.players + player)
}
