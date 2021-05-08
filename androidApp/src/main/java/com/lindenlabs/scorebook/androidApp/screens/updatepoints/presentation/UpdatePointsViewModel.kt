package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
<<<<<<< HEAD
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsViewState
=======
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
>>>>>>> Use passed in constructor values for viewmodel

class UpdatePointsViewModel(
    private val appRepository: AppRepository,
    private val gameId: String,
    private val playerId: String
) : ViewModel() {
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<Event<UpdatePointsViewEvent>> = MutableLiveData()

    init {
        launch(gameId, playerId)
    }


    fun launch(gameId: String, playerId: String) {
        viewModelScope.launch {
            game = requireNotNull(appRepository.getGame(gameId))
            player = game.players.find { it.id == playerId }!!
        }
    }

    fun handleInteraction(interaction: UpdatePointsInteraction) {
        when (interaction) {
            is UpdatePointsInteraction.ScoreIncreaseBy -> processAddToScoreAction(interaction)
            is UpdatePointsInteraction.ScoreLoweredBy -> processSubtractFromScoreAction(interaction)
        }
    }

    private fun processAddToScoreAction(interaction: UpdatePointsInteraction.ScoreIncreaseBy) {
        if (interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(Event(UpdatePointsViewEvent.AlertNoTextEntered()))
        } else {
            try {
                game.updateScore(player, interaction.point.toInt())
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

    private fun processSubtractFromScoreAction(interaction: UpdatePointsInteraction.ScoreLoweredBy) {
        if (interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(Event(UpdatePointsViewEvent.AlertNoTextEntered()))
        } else {
            try {
                player.deductFromScore(interaction.point.toInt())
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
        viewEvent.postValue(Event(UpdatePointsViewEvent.ScoreUpdated(player, game)))
}
