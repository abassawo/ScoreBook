package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.GameViewEntityMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

open class GameViewModel @Inject constructor(val appRepository: AppRepository, args: GameDetailFragmentArgs) : ViewModel() {
    val viewState: MutableLiveData<GameDetailViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<GameDetailEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    private val game: Game = args.gameArg
    private val players: List<Player> = game.players

    private var isFirstRun: Boolean = true

    init {
        when {
            isFirstRun && players.isNullOrEmpty() -> {
                isFirstRun = false
                viewEvent.postValue(ActiveGame.AddPlayersClicked(game)) // Bypass home screen, just add
            }
            players.isNullOrEmpty() -> viewState.postValue(GameDetailViewState.NotStarted(game.name))
            players.isNotEmpty() -> {
                val playerEntities = mapper.map(players) { interaction ->
                    handleInteraction(interaction)
                }
                if(game.isClosed)
                    viewState.postValue(GameDetailViewState.ClosedGame(playerEntities, game.name))
                else
                    viewState.postValue(GameDetailViewState.StartedWithPlayers(playerEntities, game.name))
            }
        }
    }

    fun handleInteraction(interaction: GameDetailInteraction) {
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
