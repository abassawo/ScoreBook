package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Player(
    val name: String,
    var scoreTotal: Int = 0,
    var rounds: List<Round> = emptyList(),
    var isPlayerTurn: Boolean = false,
    val id: UUID = UUID.randomUUID()
): Parcelable {

    fun addToScore(score: Int) {
        this.rounds += Round(score = score) // todo - keep numerical track too
        this.scoreTotal += score
    }
}