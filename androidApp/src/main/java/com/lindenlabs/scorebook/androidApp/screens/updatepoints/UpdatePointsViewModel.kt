package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UpdatePointsViewModel : ViewModel() {
    private lateinit var environment: Environment
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()

    fun launch(environment: Environment, args: UpdatePointsFragmentArgs) {
        this.environment = environment
        this.game = args.gameArg
        this.player = args.playerArg
    }

    fun handleInteraction(interaction: AddPointsInteraction) {
        when (interaction) {
            is AddPointsInteraction.AddScore -> {
                val score = interaction.point
                player.addToScore(score)
                viewEvent.postValue(UpdatePointsViewEvent.ScoreUpdated(player, game))
                viewModelScope.launch {
                    runBlocking {
                        environment.updateGame(game)
                    }
                }
            }
            is AddPointsInteraction.UndoLastScore -> Unit
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(val point: Int) : AddPointsInteraction()

        data class UndoLastScore(val scoreHistory: List<Int>) : AddPointsInteraction()
    }
}