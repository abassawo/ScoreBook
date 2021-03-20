package com.lindenlabs.scorebook.androidApp.base.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

@Database(entities = [Player::class], version = 1, exportSchema = false)
@TypeConverters(Converters.UUIDConverter::class, Converters.PlayerListConverter::class,  Converters.PlayerConverter::class, Converters.RoundConverter::class, Converters.RoundListConverter::class)
abstract class PlayersDatabase  : RoomDatabase() {
    abstract fun players(): PlayerStore

    companion object {
        @Volatile private var INSTANCE: PlayersDatabase? = null

        fun getInstance(context: Context): PlayersDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, PlayersDatabase::class.java, "players.db").build()
    }
}