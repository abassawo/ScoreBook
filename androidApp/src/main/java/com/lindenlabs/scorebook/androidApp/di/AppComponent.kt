package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.screens.home.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragment
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsFragment
import com.lindenlabs.scorebook.androidApp.screens.victory.VictoryFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface  AppComponent {
    fun inject(app: ScoreBookApplication)
    fun inject(homeFragment: HomeFragment)
    fun inject(addPlayersFragment: AddPlayersFragment)
    fun inject(updatePointsFragment: UpdatePointsFragment)
    fun inject(victoryFragment: VictoryFragment)
}
