package com.lindenlabs.scorebook.androidApp.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): GameDao
}
