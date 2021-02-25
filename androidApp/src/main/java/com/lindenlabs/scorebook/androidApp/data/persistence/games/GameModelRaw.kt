package com.lindenlabs.scorebook.androidApp.data.persistence.games

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lindenlabs.scorebook.androidApp.data.model.Player

@Entity(tableName = "games")
data class GameModelRaw(
    val name: String,
    val isClosed: Boolean){

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
