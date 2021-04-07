package com.lindenlabs.scorebook.shared.common.engines.updatepoints

import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.engines.postValue
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdatePointsEngine(private val coroutineScope: CoroutineScope , val appRepository: AppRepository) {
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableStateFlow<UpdatePointsViewState> = MutableStateFlow(UpdatePointsViewState.Loading)
    val viewEvent: MutableStateFlow<Event<UpdatePointsViewEvent>> = MutableStateFlow(Event(UpdatePointsViewEvent.Loading))

    fun launch(gameId: String, playerId: String) {
        coroutineScope.launch {
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
        if(interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(Event(UpdatePointsViewEvent.AlertNoTextEntered()))
        } else {
            try {
                game.updateScore(player, interaction.point.toInt())
            } catch (e: Exception) {

            }

            coroutineScope.launch {
                withContext(appRepository.dispatcher) {
                    runCatching { appRepository.updateGame(game) }
                        .onSuccess { onScoreUpdated(player, game) }
                }
            }
        }
    }

    private fun processSubtractFromScoreAction(interaction: UpdatePointsInteraction.ScoreLoweredBy) {
        if(interaction.point.isNullOrEmpty()) {
            viewEvent.postValue(Event(UpdatePointsViewEvent.AlertNoTextEntered()))
        } else {
            try {
                player.deductFromScore(interaction.point.toInt())
            } catch (e: Exception) {

            }

            coroutineScope.launch {
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
