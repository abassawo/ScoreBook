package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Argument
import com.lindenlabs.scorebook.androidApp.base.BaseViewModel
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UpdatePointsViewModel : BaseViewModel() {
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()

    override fun launch(environment: Environment, args: Argument) {
        super.launch(environment, args)
        val args = (args as Argument.UpdatePoints).args
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
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(val point: Int) : AddPointsInteraction()
    }
}