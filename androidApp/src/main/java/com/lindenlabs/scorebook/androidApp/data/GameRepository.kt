package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.data.persistence.GamesLocalStorage
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Round
import java.lang.IllegalStateException

class GameRepository(private val gameLocalStorage: GamesLocalStorage) : GameDataSource {

    override val games: MutableList<Game> = mutableListOf()

    override fun getAllGames(action: (games: List<Game>) -> Unit) {
        gameLocalStorage.getAllGames { games ->

            val gameEntities = games.map { Game(name = it.name) }

            action(gameEntities).also {
                this.games.clear()
                this.games.addAll(gameEntities)
            }
        }
    }

    override fun getGameById(id: Long): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) {
        games += game
    }

    override fun updateGame(game: Game, lastPlayer: Player, addedScore: Int) {
        if (!game.players.contains(lastPlayer)) throw IllegalStateException()
        else {
            lastPlayer.scoreTotal += addedScore
//            lastPlayer.rounds += Round(score = lastPlayer.scoreTotal)
        }
    }

    override fun updatePlayers(game: Game, players: List<Player>): List<Player> {
        games[games.indexOf(game)].players = players
        return game.players
    }

    override fun addPlayer(game: Game, player: Player): List<Player> =
        updatePlayers(game, game.players + player)
}
