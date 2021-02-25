package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameModelRaw(
    val name: String,
    var isClosed: Boolean = false){

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
