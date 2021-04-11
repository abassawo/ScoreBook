package com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail

import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailViewState.Loading
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailViewState.WithGameData.*
import com.lindenlabs.scorebook.shared.common.raw.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GameDetailEngine(private val coroutineScope: CoroutineScope, val appRepository: AppRepository) {
    private lateinit var game: Game
    val viewState: MutableStateFlow<GameDetailViewState> = MutableStateFlow(Loading)
    val viewEvent: MutableStateFlow<Event<GameDetailViewEvent>> = MutableStateFlow(Event(GameDetailViewEvent.None))
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()

    private var isFirstRun: Boolean = true


    fun launch(gameId: String){
        coroutineScope.launch {
            val game = requireNotNull(appRepository.getGame(gameId))
            launchGame(game)
        }
    }

    private fun launchGame(game: Game) {
        this.game = game
        when {
            isFirstRun && game.players.isNullOrEmpty() -> {
                isFirstRun = false
                viewEvent.value = Event(GameDetailViewEvent.AddPlayersClicked(game)) // Bypass home screen, just add
            }
            game.players.isNullOrEmpty() -> viewState.value = NotStarted(game)
            game.players.isNotEmpty() -> refreshScores(game)
        }
    }

    fun refreshScores(game: Game) {
        val playerEntities = mapper.map(game.players) { interaction ->
            handleInteraction(interaction)
        }
        if (game.isClosed)
            viewState.value = ClosedGame(playerEntities, game)
        else
            viewState.value = StartedWithPlayers(
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
                coroutineScope.launch {
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
