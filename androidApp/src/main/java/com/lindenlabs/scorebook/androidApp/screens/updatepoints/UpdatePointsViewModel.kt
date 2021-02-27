package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class UpdatePointsViewModel : ViewModel() {
    private val repository: GameDataSource = GameRepository()
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()
    private lateinit var game: Game
    private lateinit var player: Player

    fun launch(appNavigator: AppNavigator) {
        val (game, player) = (appNavigator.appBundle as AppNavigator.AppBundle.UpdatePointsBundle)
        this.game = game
        this.player = player
    }

    fun handleInteraction(interaction: AddPointsInteraction) {
        when (interaction) {
            is AddPointsInteraction.AddScore -> {
                val score = interaction.point
                repository.updateGame(game, player, score)
                viewEvent.postValue(UpdatePointsViewEvent.ScoreUpdated(player, game))
            }
            is AddPointsInteraction.UndoLastScore -> Unit
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(val point: Int) : AddPointsInteraction()

        data class UndoLastScore(val scoreHistory: List<Int>) : AddPointsInteraction()
    }

}