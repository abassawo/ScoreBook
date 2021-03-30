package com.lindenlabs.scorebook.shared.common.raw

import com.lindenlabs.scorebook.shared.common.data.Date
import kotlin.collections.List

typealias StalematePair = Pair<Player, Player>

data class Game(
    val id: Long = 0,
    var name: String,
    val dateCreated: Long = Date().getTime(),
    var isClosed: Boolean = false,
    var strategy: GameStrategy = GameStrategy.HighestScoreWins,
    var players: List<Player> = mutableListOf()
) {

    fun start() {
        isClosed = false
        players.forEach { it.resetScore() }
    }

    fun end(): String {
        isClosed = true

        fun GameOutcome.toText(): String = when (this) {
            is GameOutcome.WinnerAnnounced -> this.player.name + " is the winner!"
            is GameOutcome.DrawAnnounced -> "Stalemate!"
        }
        return makeResult().toText()
    }

    private fun makeResult(): GameOutcome {
        // if one max - winner
        val winners = players.getWinners(strategy)
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

sealed class GameOutcome {

    data class WinnerAnnounced(val player: Player) : GameOutcome()

    data class DrawAnnounced(val stalematePair: StalematePair) : GameOutcome()
}
