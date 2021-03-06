package com.lindenlabs.scorebook.androidApp.di

import android.content.Context
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import com.lindenlabs.scorebook.androidApp.base.domain.PersistentGameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: ScoreBookApplication) {
    @Provides
    @Singleton
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    fun provideGameDataSource(): GameDataSource = PersistentGameRepository.getInstance(application)


    @Provides
    @Singleton
    fun provideAppRepository(gameDataSource: GameDataSource) = AppRepository(gameDataSource)

}
