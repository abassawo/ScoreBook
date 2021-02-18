package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

class TestRepository : GameDataSource {
    override val games: MutableList<Game> = GameRepository.games

    init {
        GameRepository.games += (openGames + closedGames).toMutableList()
        for (game in GameRepository.games) {
            addPlayers(game, players)
        }
    }

    override fun getGameById(id: UUID): Game? =
        GameRepository.getGameById(id) ?: openGames.find { it.id == id }

    override fun storeGame(game: Game) = GameRepository.storeGame(game)

    override fun addPlayers(game: Game,  players: List<Player>): List<Player> {
        GameRepository.addPlayers(game, players)
        return getPlayers(game)
    }

    override fun getPlayers(game: Game): List<Player> = GameRepository.getPlayers(game)

    companion object {
        private val player1 = Player("player1")
        private val player2 = Player("player2")
        private val players = listOf(player1, player2)
        val openGames = listOf(Game(name = "Game1", isClosed = false))
        val closedGames = listOf(
            Game(
                name = "Game2", isClosed = true, outcome = GameOutcome.WinnerAnnounced(
                    player1
                )
            )
        )
    }
}
