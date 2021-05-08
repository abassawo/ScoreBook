package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
<<<<<<< HEAD
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailViewEvent
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailViewState
=======
import com.lindenlabs.scorebook.shared.common.domain.UserSettings
import com.lindenlabs.scorebook.shared.common.entities.gamedetail.*
import com.lindenlabs.scorebook.shared.common.raw.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
>>>>>>> Use passed in constructor values for viewmodel
import javax.inject.Inject

class GameViewModel @Inject constructor(val appRepository: AppRepository, userSettings: UserSettings, val gameId: String) : ViewModel() {
    private lateinit var game: Game
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    val viewState: MutableLiveData <GameDetailViewState> =
        MutableLiveData()
    val viewEvent: MutableLiveData<Event<GameDetailViewEvent>> =
        MutableLiveData()
    private var isFirstRun: Boolean = userSettings.isFirstRun()

    init {
        launch(gameId)
    }

    fun launch(gameId: String){
        viewModelScope.launch {
            game = requireNotNull(appRepository.getGame(gameId))
            launchGame(game)
        }
    }

    fun refresh() = launch(gameId)

    private fun launchGame(game: Game) {
        this.game = game
        when {
            isFirstRun && game.players.isNullOrEmpty() -> {
                isFirstRun = false
                viewEvent.value = Event(GameDetailViewEvent.AddPlayersClicked(game)) // Bypass home screen, just add
            }
            game.players.isNullOrEmpty() -> viewState.value =
                GameDetailViewState.WithGameData.NotStarted(game)
            game.players.isNotEmpty() -> refreshScores(game)
        }
    }

    fun refreshScores(game: Game) {
        val playerEntities = mapper.map(game.players) { interaction ->
            handleInteraction(interaction)
        }
        if (game.isClosed)
            viewState.value = GameDetailViewState.WithGameData.ClosedGame(playerEntities, game)
        else
            viewState.value = GameDetailViewState.WithGameData.StartedWithPlayers(
                playerEntities,
                game
            )

    }

    private fun <T> MutableStateFlow<T>.postValue(any: T) {
        this.value = any
    }


    fun handleInteraction(interaction: GameDetailInteraction) {
        when (interaction) {
            is GameDetailInteraction.PlayerClicked -> if (!game.isClosed) {
                viewEvent.postValue(
                    Event(GameDetailViewEvent.EditScoreForPlayer(
                        game,
                        interaction.player
                    ))
                )
            } else {
                viewEvent.postValue(Event(GameDetailViewEvent.PromptToRestartGame(game)))
            }
            is GameDetailInteraction.EndGameClicked -> confirmEndGame()
            GameDetailInteraction.GoBack -> viewEvent.postValue(Event(GameDetailViewEvent.GoBackHome))
            GameDetailInteraction.RestartGameClicked -> {
                game.start()
                viewModelScope.launch {
                    appRepository.updateGame(game)
                }
                launchGame(game)
                viewEvent.postValue(Event(GameDetailViewEvent.ShowRestartingGameMessage(game)))
            }
            GameDetailInteraction.EndGameConfirmed -> endGame(game)
            GameDetailInteraction.EditGameClicked -> viewEvent.postValue(
                Event(GameDetailViewEvent.NavigateToEditHome(
                    game
                ))
            )
            is GameDetailInteraction.RefreshScores -> refreshScores(interaction.game)
            is GameDetailInteraction.AddPlayerClicked -> navigateToAddPlayerPage()
        }
    }

    private fun confirmEndGame() = viewEvent.postValue(Event(GameDetailViewEvent.ConfirmEndGame))

    private fun endGame(game: Game) = viewEvent.postValue(Event(GameDetailViewEvent.EndGame(game)))

    fun navigateToAddPlayerPage() = viewEvent.postValue(Event(GameDetailViewEvent.AddPlayersClicked(game)))
}
