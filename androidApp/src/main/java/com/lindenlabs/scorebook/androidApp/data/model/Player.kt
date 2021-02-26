package com.lindenlabs.scorebook.androidApp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "players")
data class Player(
    val name: String,
    var scoreTotal: Int = 0,
//    var rounds: List<Round> = emptyList(),
    var isPlayerTurn: Boolean = false) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}