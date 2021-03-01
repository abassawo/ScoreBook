package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.lindenlabs.scorebook.androidApp.base.AppData
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.data.PlayerDataSource
import timber.log.Timber

class ScoreBookApplication : Application() {
    val appData: AppData = AppData(initGamesRepo())

    private fun initGamesRepo(): GameDataSource = GameRepository()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

