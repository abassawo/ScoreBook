package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewEvent
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewState
import com.lindenlabs.scorebook.shared.raw.Game
import com.lindenlabs.scorebook.shared.raw.Player
import kotlinx.coroutines.launch

class UpdatePointsViewModel() :
    ViewModel() {
//    private var game: Game
//    private var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()

    fun handleInteraction(interaction: UpdatePointsInteraction) {
        when (interaction) {
            is UpdatePointsInteraction.ScoreIncreaseBy -> processAddToScoreAction(interaction)
            is UpdatePointsInteraction.ScoreLoweredBy -> processSubtractFromScoreAction(interaction)
        }
    }

    private fun processAddToScoreAction(interaction: UpdatePointsInteraction.ScoreIncreaseBy) {
        if(interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(UpdatePointsViewEvent.AlertNoTextEntered())
        } else {
            try {
//                player.addToScore(Integer.parseInt(interaction.point))
            } catch (e: Exception) {

            }

            viewModelScope.launch {
//                withContext(appRepository.dispatcher) {
//                    runCatching { appRepository.updateGame(game) }
//                        .onSuccess { onScoreUpdated(player, game) }
//                }
            }
        }
    }

    private fun processSubtractFromScoreAction(interaction: UpdatePointsInteraction.ScoreLoweredBy) {
        if(interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(UpdatePointsViewEvent.AlertNoTextEntered())
        } else {
            try {
//                player.deductFromScore(Integer.parseInt(interaction.point))
            } catch (e: Exception) {

            }

            viewModelScope.launch {
//                withContext(appRepository.dispatcher) {
//                    runCatching { appRepository.updateGame(game) }
//                        .onSuccess { onScoreUpdated(player, game) }
//                }
            }
        }
    }

    private fun onScoreUpdated(player: Player, game: Game) =
        viewEvent.postValue(UpdatePointsViewEvent.ScoreUpdated(player, game))

    sealed class UpdatePointsInteraction {
        data class ScoreIncreaseBy(val point: String) : UpdatePointsInteraction()
        data class ScoreLoweredBy(val point: String) : UpdatePointsInteraction()
    }
}