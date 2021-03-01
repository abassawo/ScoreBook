package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.lindenlabs.scorebook.androidApp.base.AppData
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import timber.log.Timber

class ScoreBookApplication : Application() {
    val appData: AppData = AppData(initEngine(), initRepo())

    private fun initEngine(): GameEngine = GameEngine()
    private fun initRepo(): GameDataSource = GameRepository()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

