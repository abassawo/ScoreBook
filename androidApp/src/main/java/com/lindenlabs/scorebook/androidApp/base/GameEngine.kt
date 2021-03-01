package com.lindenlabs.scorebook.androidApp.base

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.StalematePair
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy

class GameEngine {

    fun startGame(game: Game) {
        game.isClosed = false
    }

    fun endGame(game: Game): String {
        game.isClosed = true

        fun GameOutcome.toText(): String = when (this) {
            is GameOutcome.WinnerAnnounced -> this.player.name + " is the winner!"
            is GameOutcome.DrawAnnounced -> "Stalemate!"
        }
        return scoreGame(game).toText()
    }

    private fun scoreGame(game: Game): GameOutcome {
        // if one max - winner
        val winners = game.players.getWinners(game.strategy)
        return when (winners.size) {
            1 -> GameOutcome.WinnerAnnounced(winners.first())
            else -> GameOutcome.DrawAnnounced(StalematePair(winners.first(), winners.last()))
        }
    }

    private fun List<Player>.getWinners(strategy: GameStrategy): List<Player> {
        var min = 0
        var max = 0
        return when (strategy) {
            GameStrategy.HighestScoreWins -> {
                for (player in this)
                    if (player.scoreTotal > max) max = player.scoreTotal
                this.filter { it.scoreTotal == max }
            }

            GameStrategy.LowestScoreWins -> {
                for (player in this)
                    if (player.scoreTotal < min) min = player.scoreTotal
                this.filter { it.scoreTotal == min }
            }
        }
    }
}