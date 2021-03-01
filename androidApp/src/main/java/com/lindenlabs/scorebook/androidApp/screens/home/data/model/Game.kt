package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import android.os.Parcelable
import androidx.room.*
import com.lindenlabs.scorebook.androidApp.data.persistence.Converters
import com.lindenlabs.scorebook.androidApp.data.persistence.Converters.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

typealias StalematePair = Pair<Player, Player>

@Parcelize
@Entity(tableName = "games")
@TypeConverters(UUIDConverter::class, PlayerListConverter::class, OutcomeConverter::class, StrategyConverter::class)
data class Game(
    @PrimaryKey
    @ColumnInfo(name="id")
    val id: UUID = UUID.randomUUID(),
    val name: String,
    var isClosed: Boolean = false,
    var strategy: GameStrategy = GameStrategy.HighestScoreWins,
    var players: List<Player> = mutableListOf(),
    var outcome: GameOutcome? = null
) : Parcelable

sealed class GameOutcome : Parcelable {

    @Parcelize
    data class WinnerAnnounced(val player: Player) : GameOutcome()

    @Parcelize
    data class DrawAnnounced(val stalematePair: StalematePair) : GameOutcome()
}