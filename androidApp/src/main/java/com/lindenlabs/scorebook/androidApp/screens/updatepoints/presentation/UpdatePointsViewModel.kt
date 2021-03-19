package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewEvent
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdatePointsViewModel(val appRepository: AppRepository, args: UpdatePointsDialogFragmentArgs) :
    ViewModel() {
    private val game: Game = args.gameArg
    private val player: Player = args.playerArg
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()

    fun handleInteraction(interaction: AddPointsInteraction) {
        when (interaction) {
            is AddPointsInteraction.ScoreAdded -> updateScore(interaction)
        }
    }

    private fun updateScore(interaction: AddPointsInteraction.ScoreAdded) {
        if(interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(UpdatePointsViewEvent.AlertNoTextEntered())
        } else {
            try {
                player.addToScore(Integer.parseInt(interaction.point))
            } catch (e: Exception) {

            }

            viewModelScope.launch {
                withContext(appRepository.dispatcher) {
                    runCatching { appRepository.updateGame(game) }
                        .onSuccess { onScoreUpdated(player, game) }
                }
            }
        }
    }

    private fun onScoreUpdated(player: Player, game: Game) =
        viewEvent.postValue(UpdatePointsViewEvent.ScoreUpdated(player, game))

    sealed class AddPointsInteraction {
        data class ScoreAdded(val point: String) : AddPointsInteraction()
    }
}