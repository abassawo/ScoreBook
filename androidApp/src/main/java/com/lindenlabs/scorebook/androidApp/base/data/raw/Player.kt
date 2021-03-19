package com.lindenlabs.scorebook.androidApp.base.data.raw

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lindenlabs.scorebook.androidApp.base.data.persistence.Converters
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@TypeConverters(Converters.PlayerConverter::class)
data class Player(
    val name: String,
    var scoreTotal: Int = 0,
    var rounds: List<Round> = mutableListOf(),
    var isPlayerTurn: Boolean = false,
    @PrimaryKey
    val id: UUID = UUID.randomUUID()
): Parcelable {

    fun addToScore(score: Int) {
        this.rounds += Round(score = score)
        this.scoreTotal += score
    }

    fun deductFromScore(score: Int) {
        if(score < 0) throw IllegalStateException()
        this.rounds += Round(score = -score)
        this.scoreTotal -= score
    }

    fun resetScore() {
        this.rounds = mutableListOf()
        this.scoreTotal = 0
    }
}

fun List<Player>.toText(): String {
    if (this.isEmpty()) return ""

    fun makeCommaText(items: List<Player>): String = buildString {
        append(items.first().name)
        for (i in 1 until items.size) {
            append(", ${items[i].name}")
        }
    }

    return when (this.size) {
        0 -> ""
        1 -> this.first().name
        else -> makeCommaText(this)
    }
}