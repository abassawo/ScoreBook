package com.lindenlabs.scorebook.androidApp.screens.addpoints

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

class AddPointsViewModel : ViewModel() {
    private val repository: GameDataSource = GameRepository
    val viewState: MutableLiveData<AddPointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPointsViewEvent> = MutableLiveData()
    private var game: Game? = null
//    private var playerEntity: PlayerEntity? = null

    fun launch(gameId: UUID, playerId: UUID) {
        val game = repository.getGameById(gameId)
        game?.let { game ->
            this.game = game
//            playerEntity = repository.getPlayersForGame(game).find { it.player.id == playerId }
        }
    }

    fun handleInteraction(interaction: AddPointsInteraction) {
        when (interaction) {
            is AddPointsInteraction.AddScore -> {
                game?.let {
                    val score = interaction.newScore
                    val game = repository.getGameById(interaction.gameId)
                    game?.let {
                        val player = repository.getPlayers(it).find { it.id == interaction.playerId }
                        player?.let { player ->
                            repository.updateGame(game, player, score)
                            viewEvent.postValue(AddPointsViewEvent.ScoreUpdated(player, game))
                        }
                    }
                }
            }
            is AddPointsInteraction.UndoLastScore -> TODO()
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