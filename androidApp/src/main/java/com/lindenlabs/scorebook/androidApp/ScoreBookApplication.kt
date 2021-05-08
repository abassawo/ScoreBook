package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.facebook.stetho.Stetho
import com.lindenlabs.scorebook.androidApp.di.AppComponent
import com.lindenlabs.scorebook.androidApp.di.AppModule
import com.lindenlabs.scorebook.androidApp.di.DaggerAppComponent
import com.lindenlabs.scorebook.shared.common.Environment
import com.lindenlabs.scorebook.shared.common.data.DatabaseFactory
import com.lindenlabs.scorebook.shared.common.data.UserSettingsStore
import timber.log.Timber

class ScoreBookApplication : Application() {
    val appComponent: AppComponent by lazy { initAppComponent() }
    val scoreBookDatabase by lazy { DatabaseFactory(this).createDB() }
    val environment by lazy { Environment(scoreBookDatabase) }
    val settings by lazy { initSettings() }

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

