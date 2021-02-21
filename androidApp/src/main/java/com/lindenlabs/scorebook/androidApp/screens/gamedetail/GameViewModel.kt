package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent.AddPlayersClicked
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent.EditScoreForPlayer
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction.EndGameClicked
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction.PlayerClicked
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

class GameViewModel : ViewModel() {
    val viewState: MutableLiveData<GameViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<GameViewEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    private val repository: GameDataSource = GameRepository
    private val gameEngine: GameEngine = GameEngine()
    private lateinit var game: Game

    private var isFirstRun: Boolean = true

    fun launch(gameId: UUID) {
        game = requireNotNull(repository.getGameById(gameId))

        val players = game.players
        if (isFirstRun && players.isNullOrEmpty()) {
            isFirstRun = false
            viewEvent.postValue(AddPlayersClicked(game)) // Bypass home screen, just add
        } else if (players.isNullOrEmpty())
            viewState.postValue(GameViewState.EmptyState(game.name))
        else if (players.isNotEmpty()) {
            val playerEntities = mapper.map(players) { interaction ->
                handleInteraction(interaction)
            }
            viewState.postValue(GameViewState.ActiveGame(playerEntities, game.name))
        }
    }

    fun handleInteraction(interaction: PlayerInteraction) {
        when (interaction) {
            is PlayerClicked -> viewEvent.postValue(EditScoreForPlayer(game, interaction.player))
            is EndGameClicked -> {
                val resultText = gameEngine.endGame(game)
                viewState.postValue(GameViewState.GameOver(resultText, game.name))
                viewEvent.postValue(GameViewEvent.GoBackHome)
            }
        }
    }

    fun navigateToAddPlayerPage() =
        game?.let { viewEvent.postValue(AddPlayersClicked(it)) }

}
