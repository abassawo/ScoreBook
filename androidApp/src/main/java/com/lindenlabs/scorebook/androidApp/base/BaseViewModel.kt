package com.lindenlabs.scorebook.androidApp.base

import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.victory.VictoryFragmentArgs

abstract class BaseViewModel : ViewModel() {
    lateinit var environment: Environment
    lateinit var argument: Argument

    open fun launch(environment: Environment, argument: Argument = Argument.None) {
        this.environment = environment
        this.argument = argument
    }
}

sealed class Argument {
    object None : Argument()
    data class AddPlayers(val args: AddPlayersFragmentArgs) : Argument()
    data class UpdatePoints(val args: UpdatePointsFragmentArgs) : Argument()
    data class GameScore(val args: GameDetailFragmentArgs) : Argument()
    data class Victory(val args: VictoryFragmentArgs) : Argument()
}
