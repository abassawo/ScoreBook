package com.lindenlabs.scorebook.shared.common.raw

import android.os.Parcel
import com.lindenlabs.scorebook.shared.common.Parcelable
import com.lindenlabs.scorebook.shared.common.data.Date
import com.lindenlabs.scorebook.shared.common.data.Id
import kotlin.collections.List

data class Player(
    val name: String,
    var scoreTotal: Int = 0,
    var rounds: List<Round> = mutableListOf(),
    var isPlayerTurn: Boolean = false,
    val id: String = Id().id,
    val dateCreated: Long = Date().getTime(),
) : Parcelable {

    constructor(jsonString: String) : this(
        name = parcel.readString()!!,
        scoreTotal = parcel.readInt(),
        isPlayerTurn = parcel.readByte() != 0.toByte(),
        id = parcel.readString()!!,
        dateCreated = parcel.readLong()
    )

    fun addToScore(score: Int) {
        this.rounds += Round(score = score)
        this.scoreTotal += score
    }

    fun deductFromScore(score: Int) {
        if (score < 0) throw IllegalStateException()
        this.rounds += Round(score = -score)
        this.scoreTotal -= score
    }

    fun resetScore() {
        this.rounds = mutableListOf()
        this.scoreTotal = 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(scoreTotal)
        parcel.writeByte(if (isPlayerTurn) 1 else 0)
        parcel.writeString(id)
        parcel.writeLong(dateCreated)
    }

    companion object CREATOR : android.os.Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
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