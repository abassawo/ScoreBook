package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.facebook.stetho.Stetho
import com.lindenlabs.scorebook.androidApp.di.AppComponent
import com.lindenlabs.scorebook.androidApp.di.AppModule
import timber.log.Timber

class ScoreBookApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initAppComponent()
        appComponent.inject(this)

//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        }
        Stetho.initializeWithDefaults(this)
    }

    private fun initAppComponent() =
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

    companion object {
        lateinit var instance: ScoreBookApplication
            private set
    }
}

