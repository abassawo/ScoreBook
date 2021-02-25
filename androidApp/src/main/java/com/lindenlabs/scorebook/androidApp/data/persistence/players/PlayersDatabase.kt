package com.lindenlabs.scorebook.androidApp.data.persistence.players

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lindenlabs.scorebook.androidApp.data.model.Player

@Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class PlayersDatabase : RoomDatabase() {
    abstract fun players(): PlayerStore
}
