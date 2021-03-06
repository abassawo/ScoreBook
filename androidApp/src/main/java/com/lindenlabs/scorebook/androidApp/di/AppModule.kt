package com.lindenlabs.scorebook.androidApp.di

import android.content.Context
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GameStore
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GamesDatabase
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
    fun provideAppRepository(gameDataSource: GameDataSource) = AppRepository(gameDataSource)

    @Provides
    @Singleton
    fun provideGameStore() : GameStore =  GamesDatabase.getInstance(application).games()

    @Provides
    @Singleton
    fun provideGameDataSource(gamesStore: GameStore): GameDataSource = PersistentGameRepository(gamesStore)

}
