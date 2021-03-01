package com.lindenlabs.scorebook.androidApp.base.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters.UUIDConverter::class, Converters.PlayerListConverter::class,  Converters.PlayerConverter::class)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun games(): GameStore

    companion object {
        @Volatile private var INSTANCE: GamesDatabase? = null

        fun getInstance(context: Context): GamesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, GamesDatabase::class.java, "games.db").build()
    }
}