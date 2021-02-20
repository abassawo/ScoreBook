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
                    val players = repository.getPlayers(it)
//                    val indexOfPlayers = players.indexOf(playerEntity)
//                    val playerData = players[indexOfPlayers]

//                    val playerWithUpdatedScore = playerData.copy(scoreTotal = score + playerData.scoreTotal)
//                    players[indexOfPlayers] = PlayerEntity(playerWithUpdatedScore, isPlayersTurn = false) {
//                        handleInteraction(AddPointsInteraction.AddScore(interaction.newScore))
//                    }
//
//                    if (indexOfPlayers + 1 >= players.size) {
//                        val nextPlayer = players[indexOfPlayers + 1].copy(isPlayersTurn = true)
//                        players[indexOfPlayers + 1] = nextPlayer
//                    } else {
//                        val nextPlayer = players[0].copy(isPlayersTurn = true)
//                        players[0] = nextPlayer
//                    }
//                    repository.storeGame(it, players)
                }
            }
            is AddPointsInteraction.UndoLastScore -> TODO()
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(val newScore: Int, val scoreHistory: List<Int> = emptyList()) :
            AddPointsInteraction()

        data class UndoLastScore(val scoreHistory: List<Int>) : AddPointsInteraction()
    }

}