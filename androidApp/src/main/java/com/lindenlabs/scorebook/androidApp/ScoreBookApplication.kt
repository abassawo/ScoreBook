package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator

class ScoreBookApplication : Application() {
    private lateinit var serviceLocator: ServiceLocator
    val appNavigator: AppNavigator by lazy { serviceLocator.appNavigator }

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator(this)
    }
}