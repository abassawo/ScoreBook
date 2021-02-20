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
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
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
            }
            else if(players.isNullOrEmpty()) {
                viewState.postValue(GameViewState.EmptyState(game.name))
            } else if (players.isNotEmpty()) {
                val playerEntities = mapper.map(players) {
                        interaction -> handleInteraction(interaction)
                }
                viewState.postValue(GameViewState.ActiveGame(playerEntities, game.name))
            }
        }
    }

    private fun handleInteraction(interaction: PlayerInteraction) {
        when (interaction) {
            is PlayerInteraction.PlayerClicked -> {
                game?.let {
                    viewEvent.postValue(GameViewEvent.EditScoreForPlayer(it, interaction.player))
                }
            }
        }
    }


    fun navigateToAddPlayerPage() {
        game?.let { viewEvent.postValue(AddPlayersClicked(it)) }
    }
}
