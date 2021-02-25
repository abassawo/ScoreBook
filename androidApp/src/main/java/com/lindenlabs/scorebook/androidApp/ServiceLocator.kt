package com.lindenlabs.scorebook.androidApp

import androidx.room.Room
import com.lindenlabs.scorebook.androidApp.data.persistence.GamesDatabase
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import android.content.Context
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.data.PlayerRepository
import com.lindenlabs.scorebook.androidApp.data.persistence.GamesLocalStorage

class ServiceLocator(applicationContext: Context) {
    var appNavigator: AppNavigator

    private val gamesDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            GamesDatabase::class.java,
            "games.db"
        ).build()
    }

    private val gamesLocalDataSource = GamesLocalStorage(gamesDatabase.games())

    init {
        appNavigator = AppNavigator(
            GameRepository(gamesLocalDataSource),
            PlayerRepository()
        )
    }

}
