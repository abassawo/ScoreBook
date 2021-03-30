package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.SubComponents.*
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun homeFragmentComponent(): HomeFragmentComponent
    fun addPlayersComponentBuilder(): AddPlayersFragmentComponent.Builder
    fun updatePointsComponentBuilder(): UpdatePointsFragmentComponent.Builder
    fun victoryFragmentComponentBuilder(): VictoryFragmentComponent.Builder
    fun gameScoreComponentBuilder(): GameScoreComponent.Builder
    fun editGameComponentBuilder() : EditGameComponent.Builder
}
