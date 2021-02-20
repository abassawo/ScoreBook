package com.lindenlabs.scorebook.androidApp.screens.addpoints

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import java.util.*

class AddPointsViewModel : ViewModel() {
    private val repository: GameDataSource = GameRepository
    val viewState: MutableLiveData<AddPointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPointsViewEvent> = MutableLiveData()
    private lateinit var game: Game
    private lateinit var player: Player

    fun launch(gameId: UUID, playerId: UUID) {
        game = repository.getGameById(gameId) as Game
        player = game.players.find { it.id == playerId } as Player
        this.game = game
    }

    fun handleInteraction(interaction: AddPointsInteraction) {
        when (interaction) {
            is AddPointsInteraction.AddScore -> {
                val score = interaction.newScore
                repository.updateGame(game, player, score)
                viewEvent.postValue(AddPointsViewEvent.ScoreUpdated(player, game))
            }
            is AddPointsInteraction.UndoLastScore -> Unit
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(
            val newScore: Int,
            val playerId: UUID,
            val gameId: UUID
        ) :
            AddPointsInteraction()

        data class UndoLastScore(val scoreHistory: List<Int>) : AddPointsInteraction()
    }

}