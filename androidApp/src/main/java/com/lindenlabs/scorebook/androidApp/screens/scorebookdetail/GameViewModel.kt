package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent.AddPlayersClicked
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent.EditScoreForPlayer
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewState

class GameViewModel(args: GameDetailFragmentArgs) : ViewModel() {
    val viewState: MutableLiveData<ScoreBookViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<ScoreBookViewEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    private val game: Game = args.gameArg
    private val players: List<Player> = game.players

    private var isFirstRun: Boolean = true

    init {
        if (isFirstRun && players.isNullOrEmpty()) {
            isFirstRun = false
            viewEvent.postValue(AddPlayersClicked(game)) // Bypass home screen, just add
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
            is PlayerClicked -> viewEvent.postValue(EditScoreForPlayer(game, interaction.player))
            is EndGameClicked -> viewEvent.postValue(ScoreBookViewEvent.EndGame(game))
            GoBack -> viewEvent.postValue(ScoreBookViewEvent.GoBackHome)
        }
    }

    fun navigateToAddPlayerPage() =
       viewEvent.postValue(AddPlayersClicked(game))

}
