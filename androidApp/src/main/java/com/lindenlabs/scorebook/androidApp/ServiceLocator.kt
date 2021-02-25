package com.lindenlabs.scorebook.androidApp

import androidx.room.Room
import com.lindenlabs.scorebook.androidApp.data.persistence.AppDatabase
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import android.content.Context

class ServiceLocator(applicationContext: Context, val appNavigator: AppNavigator = AppNavigator(), ) {

    private val logsDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "logging.db"
    ).build()

    private val gamesLocalDataSource = GamesLocalDataSource(logsDatabase.logDao())

}
