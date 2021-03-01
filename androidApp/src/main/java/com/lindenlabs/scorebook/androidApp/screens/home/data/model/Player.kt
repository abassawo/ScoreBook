package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.lindenlabs.scorebook.androidApp.data.persistence.Converters
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@TypeConverters(Converters.PlayerConverter::class)
data class Player(
    val name: String,
    var scoreTotal: Int = 0,
    var rounds: List<Round> = emptyList(),
    var isPlayerTurn: Boolean = false,
    @PrimaryKey
    val id: UUID = UUID.randomUUID()
): Parcelable {

    fun addToScore(score: Int) {
        this.rounds += Round(score = score)
        this.scoreTotal += score
    }
}