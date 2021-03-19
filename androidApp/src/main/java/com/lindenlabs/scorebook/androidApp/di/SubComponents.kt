package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.di.scope.FragmentScope
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragment
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.ActiveGameDetailFragment
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsFragment
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryFragment
import dagger.Subcomponent

interface SubComponents {

    @FragmentScope
    @Subcomponent(modules=[HomeModule::class])
    interface HomeFragmentComponent {
        fun inject(fragment: HomeFragment)
    }

    @FragmentScope
    @Subcomponent(modules = [AddPlayersArgsModule::class])
    interface AddPlayersFragmentComponent {
        fun inject(fragment: AddPlayersFragment)

        @Subcomponent.Builder
        interface Builder {
            fun plus(module: AddPlayersArgsModule): Builder
            fun build(): AddPlayersFragmentComponent
        }
    }

    @FragmentScope
    @Subcomponent(modules= [GameScoreModule::class])
    interface GameScoreComponent {
        fun inject(fragment: ActiveGameDetailFragment)

        @Subcomponent.Builder
        interface Builder {
            fun plus(module: GameScoreModule): Builder
            fun build(): GameScoreComponent
        }
    }

    @FragmentScope
    @Subcomponent(modules = [UpdatePointsModule::class])
    interface UpdatePointsFragmentComponent {
        fun inject(fragment: UpdatePointsFragment)

        @Subcomponent.Builder
        interface Builder {
            fun plus(module: UpdatePointsModule): Builder
            fun build(): UpdatePointsFragmentComponent
        }
    }

    @FragmentScope
    @Subcomponent(modules = [VictoryModule::class])
    interface VictoryFragmentComponent {
        fun inject(fragment: VictoryFragment)

        @Subcomponent.Builder
        interface Builder {
            fun plus(module: VictoryModule): Builder
            fun build(): VictoryFragmentComponent
        }
    }

}