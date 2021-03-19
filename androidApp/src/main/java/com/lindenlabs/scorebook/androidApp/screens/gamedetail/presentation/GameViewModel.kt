package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.GameViewEntityMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

open class GameViewModel @Inject constructor(val appRepository: AppRepository, args: ActiveGameDetailFragmentArgs) : ViewModel() {
    val viewState: MutableLiveData<ScoreBookViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<GameDetailEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    private val game: Game = args.gameArg
    private val players: List<Player> = game.players

    private var isFirstRun: Boolean = true

    init {
        if (isFirstRun && players.isNullOrEmpty()) {
            isFirstRun = false
            viewEvent.postValue(ActiveGame.AddPlayersClicked(game)) // Bypass home screen, just add
        } else if (players.isNullOrEmpty())
            viewState.postValue(ScoreBookViewState.EmptyState(game.name))
        else if (players.isNotEmpty()) {
            val playerEntities = mapper.map(players) { interaction ->
                handleInteraction(interaction)
            }
            viewState.postValue(ScoreBookViewState.ActiveGame(playerEntities, game.name))
        }
    }

    fun handleInteraction(interaction: ScoreBookInteraction) {
        when (interaction) {
            is PlayerClicked -> viewEvent.postValue(
                ActiveGame.EditScoreForPlayer(
                    game,
                    interaction.player
                )
            )
            is EndGameClicked -> viewEvent.postValue(ActiveGame.EndGame(game))
            GoBack -> viewEvent.postValue(ActiveGame.GoBackHome)
            RestartGameClicked -> {
                game.start()
                viewModelScope.launch {
                    appRepository.updateGame(game)
                }
                viewEvent.postValue(ClosedGame.RestartGame(game))
            }
        }
    }

    fun navigateToAddPlayerPage() =
       viewEvent.postValue(ActiveGame.AddPlayersClicked(game))

}
