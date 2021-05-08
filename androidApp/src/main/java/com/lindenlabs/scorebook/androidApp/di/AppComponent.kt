package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.SubComponents.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun componentBuilder(): SubComponent.Builder
}
