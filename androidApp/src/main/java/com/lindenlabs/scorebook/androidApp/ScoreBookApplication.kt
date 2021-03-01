package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.lindenlabs.scorebook.androidApp.base.AppData
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameDatabaseHandler
import com.lindenlabs.scorebook.androidApp.data.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.data.persistence.GamesDatabase
import timber.log.Timber

class ScoreBookApplication : Application() {
    val appData: AppData by lazy { AppData(initPersistentGameRepo()) }

    private fun initPersistentGameRepo(): GameDataSource =
        PersistentGameRepository(gamesDatabase = initGameHandler())
            .also { it.load {  } }

    private fun initGameHandler(): GameDatabaseHandler {
        return GameDatabaseHandler(initGamesDatabase().games())
    }

    private fun initGamesDatabase() = GamesDatabase.getInstance(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

