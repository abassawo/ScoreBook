package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.facebook.stetho.Stetho
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GamesDatabase
import com.lindenlabs.scorebook.androidApp.base.data.persistence.PlayersDatabase
import com.lindenlabs.scorebook.androidApp.base.data.sources.GameDataSource
import com.lindenlabs.scorebook.androidApp.base.data.sources.LocalGameDataSource
import com.lindenlabs.scorebook.androidApp.base.data.sources.LocalPlayerDataSource
import com.lindenlabs.scorebook.androidApp.base.data.sources.PlayerDataSource
import com.lindenlabs.scorebook.androidApp.di.AppComponent
import com.lindenlabs.scorebook.androidApp.di.AppModule
import com.lindenlabs.scorebook.androidApp.di.DaggerAppComponent
import com.lindenlabs.scorebook.androidApp.settings.UserSettingsStore
import timber.log.Timber

class ScoreBookApplication : Application() {
    val appComponent: AppComponent by lazy { initAppComponent() }
    val appRepository: AppRepository by lazy { AppRepository(initRepo(), initPlayersRepo(), initSettings()) }

    private fun initRepo(): GameDataSource {
        val gamesStore = GamesDatabase.getInstance(this).games()
        return LocalGameDataSource(gamesStore)
    }

    private fun initPlayersRepo(): PlayerDataSource {
        val gamesStore = PlayersDatabase.getInstance(this).players()
        return LocalPlayerDataSource(gamesStore)
    }


    private fun initSettings() = UserSettingsStore(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
    }

    private fun initAppComponent() =
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
}

