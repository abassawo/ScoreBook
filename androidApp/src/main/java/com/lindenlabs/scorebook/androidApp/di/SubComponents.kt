package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameFragment
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragment
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragment
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsDialogFragment
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryFragment
import dagger.Subcomponent

interface SubComponents {

    @FragmentScope
    @Subcomponent(modules = [ArgModule::class])
    interface SubComponent {
        fun inject(fragment: AddPlayersFragment)
        fun inject(fragment: HomeFragment)
        fun inject(fragment: GameDetailFragment)
        fun inject(fragment: VictoryFragment)
        fun inject(fragment: EditGameFragment)
        fun inject(fragment: UpdatePointsDialogFragment)

        @Subcomponent.Builder
        interface Builder {
            fun plus(module: ArgModule): Builder
            fun build(): SubComponent
        }
    }

}