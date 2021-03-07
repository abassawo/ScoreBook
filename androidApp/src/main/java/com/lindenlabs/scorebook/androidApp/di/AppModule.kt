package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: ScoreBookApplication) {

    @Provides
    @Singleton
    fun provideEnvironment() = application.environment

}
