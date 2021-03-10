package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.screens.home.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragment
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
    fun addPlayersFragmentComponent(): AddPlayersFragmentComponent
    fun updatePointsFragmentComponent(): UpdatePointsFragmentComponent
    fun victoryFragmentComponent(): VictoryFragmentComponent
}

@Subcomponent
interface HomeFragmentComponent {
    fun inject(fragment: HomeFragment)
}

@Subcomponent
interface AddPlayersFragmentComponent {
    fun inject(fragment: AddPlayersFragment)
}

@Subcomponent
interface UpdatePointsFragmentComponent {
    fun inject(fragment: UpdatePointsFragment)
}

@Subcomponent
interface VictoryFragmentComponent {
    fun inject(fragment: VictoryFragment)
}