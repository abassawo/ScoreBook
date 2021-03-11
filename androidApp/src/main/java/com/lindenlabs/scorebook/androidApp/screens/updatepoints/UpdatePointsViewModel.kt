package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UpdatePointsViewModel(val environment: Environment, val args: UpdatePointsFragmentArgs) : ViewModel() {
    private val game: Game = args.gameArg
    private val  player: Player = args.playerArg
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()

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
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(val point: Int) : AddPointsInteraction()
    }
}