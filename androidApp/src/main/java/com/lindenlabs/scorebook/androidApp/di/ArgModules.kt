package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.domain.UserSettings
import dagger.Module
import dagger.Provides

@Module
class ArgModule(val payload: ArgumentPayload) {

    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository, userSettings: UserSettings): ViewModelFactory =
        ViewModelFactory(appRepository, userSettings, payload)
}

