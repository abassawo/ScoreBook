package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class ScoreBookApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(this)
    }

}

