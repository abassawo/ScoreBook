package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.data.UserSettingsStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: ScoreBookApplication) {

    @Provides
    @Singleton
    fun provideViewModelFactory(appRepository: AppRepository, userSettingsStore: UserSettingsStore): ViewModelFactory =
        ViewModelFactory(appRepository, userSettingsStore)

    @Provides
    @Singleton
    fun provideEnvironment() = application.environment

    @Provides
    @Singleton
    fun provideUserSettingsStore() = UserSettingsStore(application)

    @Provides
    @Singleton
    fun provideAppRepository(): AppRepository = with(application.scoreBookDatabase) {
        AppRepository(this.gameHistoryQueries, this.playerHistoryQueries)
    }
}
