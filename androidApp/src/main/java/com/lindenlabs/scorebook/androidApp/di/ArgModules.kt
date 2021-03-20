package com.lindenlabs.scorebook.androidApp.di

import androidx.navigation.NavArgs
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsDialogFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryFragmentArgs
import dagger.Module
import dagger.Provides

@Module
class HomeModule {
    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, object : NavArgs {})
}


@Module
class AddPlayersArgsModule(private val addPlayerArgs: AddPlayersFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = addPlayerArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, addPlayerArgs)
}


@Module
class UpdatePointsModule(private val updatePointsArgs: UpdatePointsDialogFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs= updatePointsArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, updatePointsArgs)
}


@Module
class GameScoreModule(private val gameDetailFragmentArgs: GameDetailFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = gameDetailFragmentArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, gameDetailFragmentArgs)
}

@Module
class EditGameModule(private val editGameFragmentArgs: EditGameFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = editGameFragmentArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, editGameFragmentArgs)

}

@Module
class VictoryModule(private val victoryFragmentArgs: VictoryFragmentArgs) {

    @Provides
    @FragmentScope
    fun provideArg(): NavArgs = victoryFragmentArgs

    @Provides
    @FragmentScope
    fun provideViewModelFactory(appRepository: AppRepository): ViewModelFactory =
        ViewModelFactory(appRepository, victoryFragmentArgs)
}




