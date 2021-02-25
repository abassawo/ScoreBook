package com.lindenlabs.scorebook.androidApp.data.persistence.games

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameModelRaw::class], version = 1, exportSchema = false)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun games(): GameStore
}
