package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.GameViewEntityMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

open class GameViewModel @Inject constructor(
    val appRepository: AppRepository,
    val args: GameDetailFragmentArgs
) : ViewModel() {
    val viewState: MutableLiveData<GameDetailViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<GameDetailViewEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()

    private var isFirstRun: Boolean = true
    private lateinit var game: Game
    private lateinit var players: List<Player>

    init {
        launch(args.gameArg)
    }

    fun launch(game: Game) {
        this.game = game
        this.players = game.players
        val players: List<Player> = game.players
        when {
            isFirstRun && players.isNullOrEmpty() -> {
                isFirstRun = false
                viewEvent.postValue(AddPlayersClicked(game)) // Bypass home screen, just add
            }
            players.isNullOrEmpty() -> viewState.postValue(GameDetailViewState.NotStarted(game))
            players.isNotEmpty() -> refreshScore()
        }
    }

    fun refreshScore() {
        val playerEntities = mapper.map(players) { interaction ->
            handleInteraction(interaction)
        }
        if (game.isClosed)
            viewState.postValue(GameDetailViewState.ClosedGame(playerEntities, game))
        else
            viewState.postValue(
                GameDetailViewState.StartedWithPlayers(
                    playerEntities,
                    game
                )
            )
    }

    fun handleInteraction(interaction: GameDetailInteraction) {
        val game = args.gameArg
        when (interaction) {
            is PlayerClicked -> if (!game.isClosed) {
                viewEvent.postValue(EditScoreForPlayer(game, interaction.player))
            } else {
               viewEvent.postValue(PromptToRestartGame(game))
            }
            is EndGameClicked -> confirmEndGame()
            GoBack -> viewEvent.postValue(GoBackHome)
            RestartGameClicked -> {
                game.start()
                viewModelScope.launch {
                    appRepository.updateGame(game)
                }
                launch(game)
                viewEvent.postValue(ShowRestartingGameMessage(game))
            }
            EndGameConfirmed -> endGame(game)
            EditGameClicked -> viewEvent.postValue(NavigateToEditHome(game))
        }
    }

    private fun confirmEndGame() = viewEvent.postValue(ConfirmEndGame)

    private fun endGame(game: Game) =  viewEvent.postValue(EndGame(game))

    fun navigateToAddPlayerPage() =
        viewEvent.postValue(AddPlayersClicked(args.gameArg))

}
