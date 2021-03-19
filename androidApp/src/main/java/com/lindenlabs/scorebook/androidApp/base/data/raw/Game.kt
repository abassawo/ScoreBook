package com.lindenlabs.scorebook.androidApp.base.data.raw

import android.os.Parcelable
import androidx.room.*
import com.lindenlabs.scorebook.androidApp.base.data.persistence.Converters.*
import kotlinx.android.parcel.Parcelize
import java.util.*

typealias StalematePair = Pair<Player, Player>

@Parcelize
@Entity(tableName = "games")
@TypeConverters(UUIDConverter::class, PlayerConverter::class, OutcomeConverter::class, StrategyConverter::class)
data class Game(
    @PrimaryKey
    @ColumnInfo(name="id")
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val dateCreated: Long = Date().time,
    var isClosed: Boolean = false,
    var strategy: GameStrategy = GameStrategy.HighestScoreWins,
    var players: List<Player> = mutableListOf()
) : Parcelable {

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

sealed class GameOutcome : Parcelable {

    @Parcelize
    data class WinnerAnnounced(val player: Player) : GameOutcome()

    @Parcelize
    data class DrawAnnounced(val stalematePair: StalematePair) : GameOutcome()
}
