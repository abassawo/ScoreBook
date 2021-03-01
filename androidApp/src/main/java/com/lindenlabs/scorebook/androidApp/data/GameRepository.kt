package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Round
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import java.lang.IllegalStateException
import java.util.*

class GameRepository : GameDataSource {
    var games: MutableList<Game> = mutableListOf()

    override fun load(callback: (PairOfOpenToClosedGames) -> Unit) {
        val openGames = GetOpenGames(games).invoke()
        val closedGames = GetClosedGames(games).invoke()
        callback(openGames to closedGames)
    }

    override fun getGameById(id: UUID): Game? = games.find { it.id == id }

    override fun storeGame(game: Game) {
        games = games.plus(game).toMutableList()
    }

    override fun roundPlayed(game: Game, lastPlayer: Player, addedScore: Int) {
        if(!game.players.contains(lastPlayer)) throw IllegalStateException()
        else {
            lastPlayer.scoreTotal += addedScore
            lastPlayer.rounds += Round(score = lastPlayer.scoreTotal)
        }
    }

//    override fun updatePlayers(game: Game, players: List<Player>) : List<Player> {
//        game.players = players
//        updateGame(game)
//        return game.players
//    }

    override fun updateGame(game: Game) {
        games[games.indexOf(game)] = game
    }

    override fun clear() = games.clear()
}
