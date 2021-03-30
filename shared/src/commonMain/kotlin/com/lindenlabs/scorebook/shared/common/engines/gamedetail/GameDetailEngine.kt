package com.lindenlabs.scorebook.shared.common.engines.gamedetail

import com.lindenlabs.scorebook.shared.common.Environment.appRepository
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState.*
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState.WithGameData.*
import com.lindenlabs.scorebook.shared.common.raw.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GameDetailEngine(private val coroutineScope: CoroutineScope) {
    private lateinit var game: Game
    val viewState: MutableStateFlow<GameDetailViewState> = MutableStateFlow(Nil)
    val viewEvent: MutableStateFlow<GameDetailViewEvent> = MutableStateFlow(GameDetailViewEvent.Nil)
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()

    private var isFirstRun: Boolean = true


    fun launch(game: Game) {
        this.game = game
        when {
            isFirstRun && game.players.isNullOrEmpty() -> {
                isFirstRun = false
                viewEvent.value =
                    GameDetailViewEvent.AddPlayersClicked(game) // Bypass home screen, just add
            }
            game.players.isNullOrEmpty() -> viewState.value = NotStarted(game)
            game.players.isNotEmpty() -> refreshScore()
        }
    }

    fun refreshScore() {
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
                    GameDetailViewEvent.EditScoreForPlayer(
                        game,
                        interaction.player
                    )
                )
            } else {
                viewEvent.postValue(GameDetailViewEvent.PromptToRestartGame(game))
            }
            is GameDetailInteraction.EndGameClicked -> confirmEndGame()
            GameDetailInteraction.GoBack -> viewEvent.postValue(GameDetailViewEvent.GoBackHome)
            GameDetailInteraction.RestartGameClicked -> {
                game.start()
                coroutineScope.launch {
                    appRepository.updateGame(game)
                }
                launch(game)
                viewEvent.postValue(GameDetailViewEvent.ShowRestartingGameMessage(game))
            }
            GameDetailInteraction.EndGameConfirmed -> endGame(game)
            GameDetailInteraction.EditGameClicked -> viewEvent.postValue(
                GameDetailViewEvent.NavigateToEditHome(
                    game
                )
            )
        }
    }

    private fun confirmEndGame() = viewEvent.postValue(GameDetailViewEvent.ConfirmEndGame)

    private fun endGame(game: Game) = viewEvent.postValue(GameDetailViewEvent.EndGame(game))

    fun navigateToAddPlayerPage() =
        viewEvent.postValue(GameDetailViewEvent.AddPlayersClicked(game))
}
