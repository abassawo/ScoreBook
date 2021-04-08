package com.lindenlabs.scorebook.shared.common.raw

import com.lindenlabs.scorebook.shared.common.data.Date
import com.lindenlabs.scorebook.shared.common.data.Id
import com.lindenlabs.scorebook.shared.common.data.PlayerListConverter
import comlindenlabsscorebooksharedcommon.Games
import kotlin.collections.List

data class Game(
    val id: String = Id().id,
    var name: String,
    val dateCreated: Long = Date().getTime(),
    var isClosed: Boolean = false,
    var strategy: GameStrategy = GameStrategy.HighestScoreWins,
    val players: MutableList<Player> = mutableListOf()
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
            else -> GameOutcome.DrawAnnounced
        }
    }

    private fun List<Player>.getWinners(strategy: GameStrategy): List<Player> {
        return when (strategy) {
            GameStrategy.HighestScoreWins -> {
                var max = Int.MIN_VALUE
                for (player in this)
                    if (player.scoreTotal > max) max = player.scoreTotal
                this.filter { it.scoreTotal == max }
            }

            GameStrategy.LowestScoreWins -> {
                var min = Int.MAX_VALUE
                for (player in this)
                    if (player.scoreTotal < min) min = player.scoreTotal
                this.filter { it.scoreTotal == min }
            }
        }
    }

    fun toDao(): Games = Games(
        id = this.id,
        name = this.name,
        dateCreated = this.dateCreated,
        strategy = this.strategy.name,
        players = PlayerListConverter.playerToString(this.players),
        isClosed = this.isClosed
    )

    fun updateScore(player: Player, score: Int) {
        player.addToScore(score)
        this.players[players.indexOf(player)] = player
    }
}

sealed class GameOutcome {

    data class WinnerAnnounced(val player: Player) : GameOutcome()

    object DrawAnnounced : GameOutcome()
}
