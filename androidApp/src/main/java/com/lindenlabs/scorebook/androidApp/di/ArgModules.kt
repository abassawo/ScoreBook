package com.lindenlabs.scorebook.androidApp.di

import androidx.navigation.NavArgs
import com.lindenlabs.scorebook.androidApp.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.victory.VictoryFragmentArgs
import dagger.Module
import dagger.Provides

@Module
class HomeModule {
    @Provides
    @FragmentScope
    fun provideViewModelFactory(environment: Environment): ViewModelFactory =
        ViewModelFactory(environment, object : NavArgs {})
}


@Module
class AddPlayersArgsModule(private val addPlayerArgs: AddPlayersFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = addPlayerArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(environment: Environment): ViewModelFactory  =
        ViewModelFactory(environment, addPlayerArgs)
}


@Module
class UpdatePointsModule(private val updatePointsArgs: UpdatePointsFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs= updatePointsArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(environment: Environment): ViewModelFactory  =
        ViewModelFactory(environment, updatePointsArgs)
}


@Module
class GameScoreModule(private val gameDetailFragmentArgs: GameDetailFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = gameDetailFragmentArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(environment: Environment): ViewModelFactory  =
        ViewModelFactory(environment, gameDetailFragmentArgs)
}

@Module
class VictoryModule(private val victoryFragmentArgs: VictoryFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = victoryFragmentArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(environment: Environment): ViewModelFactory  =
        ViewModelFactory(environment, victoryFragmentArgs)
}




