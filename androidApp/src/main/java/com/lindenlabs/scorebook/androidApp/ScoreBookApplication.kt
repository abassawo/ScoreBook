package com.lindenlabs.scorebook.androidApp

import android.app.Application
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator

class ScoreBookApplication : Application() {
    private lateinit var serviceLocator: ServiceLocator
    lateinit var appNavigator: AppNavigator

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator()
        appNavigator = serviceLocator.appNavigator
    }
}