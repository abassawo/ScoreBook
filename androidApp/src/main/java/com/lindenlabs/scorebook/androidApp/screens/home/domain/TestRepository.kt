package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

class TestRepository : GameRepository() {
    override fun getGames(): List<Game> = openGames

    override fun getGameById(id: UUID): Game? = openGames.find { it.id == id }

    companion object {
        private val player1 = Player("player1")
        private val player2 = Player("player2")
        private val players = listOf(player1, player2)
        val openGames = listOf(Game(name = "Game1", isInSession = true, players = players))
        val closedGames = listOf(Game(name = "Game2", isInSession = false, players = players, outcome = GameOutcome.WinnerAnnounced(
            player1)))
    }

}
