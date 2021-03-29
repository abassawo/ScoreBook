package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.settings.UserSettingsStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: ScoreBookApplication) {

    @Provides
    @Singleton
    fun provideUserSettingsStore() = UserSettingsStore(application)
}
