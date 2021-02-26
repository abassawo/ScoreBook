package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.data.model.Player
import com.lindenlabs.scorebook.androidApp.data.persistence.GamesLocalStorage
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
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

    override fun endGame(game: Game) {
        game.isClosed = true
        gameLocalStorage.updateGame(game.toRaw())
    }

    override fun getGameById(id: Long): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) {
        gameLocalStorage.addGame(game.toRaw())
    }

    override fun updateGame(game: Game, lastPlayer: Player, addedScore: Int) {
        if (!game.players.contains(lastPlayer)) throw IllegalStateException()
        else {
            lastPlayer.scoreTotal += addedScore
//            lastPlayer.rounds += Round(score = lastPlayer.scoreTotal)
        }
        gameLocalStorage.updateGame(game.toRaw())
    }

    override fun updatePlayers(game: Game, players: List<Player>): List<Player> {
        game.players = players
        gameLocalStorage.updateGame(game.toRaw())
        return game.players
    }

    override fun addPlayer(game: Game, player: Player): List<Player> =
        updatePlayers(game, game.players + player)
}
