package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.di.SubComponents.*
import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.androidApp.screens.home.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragment
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragment
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsFragment
import com.lindenlabs.scorebook.androidApp.screens.victory.VictoryFragment
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(app: ScoreBookApplication)
    fun homeFragmentComponent(): HomeFragmentComponent
    fun addPlayersComponentBuilder(): AddPlayersFragmentComponent.Builder
    fun updatePointsComponentBuilder(): UpdatePointsFragmentComponent.Builder
    fun victoryFragmentComponentBuilder(): VictoryFragmentComponent.Builder
    fun gameScoreComponentBuilder(): GameScoreComponent.Builder
}
