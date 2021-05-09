package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel.PointsDirection.*
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository

import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import kotlinx.coroutines.launch
import timber.log.Timber

class UpdatePointsViewModel(private val repo: AppRepository, gameId: String, playerId: String) :
    ViewModel() {
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<Event<UpdatePointsViewEvent>> = MutableLiveData()

    init {
        launch(gameId, playerId)
    }

    fun launch(gameId: String, playerId: String) {
        viewModelScope.launch {
            game = requireNotNull(repo.getGame(gameId))
            player = game.players.find { it.id == playerId }!!
        }
    }

    fun handleInteraction(interaction: UpdatePointsInteraction) {
        val direction = when (interaction) {
            is UpdatePointsInteraction.ScoreIncreaseBy -> UP
            is UpdatePointsInteraction.ScoreLoweredBy -> DOWN
        }
        updateScoreFor(player, direction, interaction.point)
    }

    private fun updateScoreFor(player: Player, direction: PointsDirection, pointsText: String) {
        if (pointsText.isEmpty())
            viewEvent.postValue(Event(UpdatePointsViewEvent.AlertNoTextEntered()))

        suspend fun addOrMinusScore(delta: Int) =
            when (direction) {
                UP -> player.addToScore(delta)
                DOWN -> player.deductFromScore(delta)
            }.also { updateGame(game) }

        viewModelScope.launch {
            runCatching { addOrMinusScore(pointsText.toInt()) }
                .onSuccess { onScoreUpdated(player, game) }
                .onFailure { Timber.e(it) }
        }
    }

    private suspend fun updateGame(game: Game) = repo.updateGame(game)

    private fun onScoreUpdated(player: Player, game: Game) =
        viewEvent.postValue(Event(UpdatePointsViewEvent.ScoreUpdated(player, game)))

    enum class PointsDirection {
        UP, DOWN
    }
}
