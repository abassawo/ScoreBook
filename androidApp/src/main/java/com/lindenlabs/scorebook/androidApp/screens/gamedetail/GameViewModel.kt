package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent.AddPlayersClicked
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.StalematePair
import java.util.*

class GameViewModel : ViewModel() {
    val viewState: MutableLiveData<GameViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<GameViewEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    private val repository: GameDataSource = GameRepository

    private var game: Game? = null

    private var isFirstRun: Boolean = true // todo - manage with shared prefs

    fun launch(gameId: UUID) {
        val game = repository.getGameById(gameId)
        game?.let {
            this.game = it
            val players = repository.getPlayers(it)
            if (isFirstRun && players.isNullOrEmpty()) {
                isFirstRun = false
                viewEvent.postValue(AddPlayersClicked(it)) // Bypass home screen, just add
            } else if (players.isNullOrEmpty()) {
                viewState.postValue(GameViewState.EmptyState(game.name))
            } else if (players.isNotEmpty()) {
                val playerEntities = mapper.map(players) { interaction ->
                    handleInteraction(interaction)
                }
                viewState.postValue(GameViewState.ActiveGame(playerEntities, game.name))
            }
        }
    }

    fun handleInteraction(interaction: PlayerInteraction) {
        game?.let {
            when (interaction) {
                is PlayerInteraction.PlayerClicked -> {
                    viewEvent.postValue(
                        GameViewEvent.EditScoreForPlayer(
                            it,
                            interaction.player
                        )
                    )
                }
                is PlayerInteraction.EndGamerClicked -> {
                    val outcome = scoreGame(it)
                    val resultText = outcome.toText()
                    viewState.postValue(GameViewState.GameOver(resultText, it.name))
                }
            }
        }
    }


    private fun GameOutcome.toText(): String {
        return when(this) {
            is GameOutcome.WinnerAnnounced -> this.player.name + " is the winner!"
            is GameOutcome.DrawAnnounced -> "Stalemate!"
        }
    }

    private fun scoreGame(game: Game): GameOutcome {
        // if one max - winner
        val players = repository.getPlayers(game)
        var max = 0
        for (player in players) {
            val score = player.scoreTotal
            if (score > max) {
                max = score
            }
        }
        val winners = players.filter { it.scoreTotal == max }
        return when (winners.size) {
            1 -> GameOutcome.WinnerAnnounced(winners.first())
            else -> GameOutcome.DrawAnnounced(StalematePair(winners.first(), winners.last()))
        }
    }


    fun navigateToAddPlayerPage() =
        game?.let { viewEvent.postValue(AddPlayersClicked(it)) }

}
