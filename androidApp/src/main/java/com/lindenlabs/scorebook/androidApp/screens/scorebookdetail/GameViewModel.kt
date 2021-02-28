package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent.AddPlayersClicked
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent.EditScoreForPlayer
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction.EndGameClicked
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction.PlayerClicked
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

class GameViewModel : ViewModel() {
    val viewState: MutableLiveData<ScoreBookViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<ScoreBookViewEvent> = MutableLiveData()
    private val mapper: GameViewEntityMapper = GameViewEntityMapper()
    private val gameEngine: GameEngine = GameEngine()
    private lateinit var game: Game

    private var isFirstRun: Boolean = true

    fun launch() {
//        val destination = (appNavigator.appBundle as GameDetailBundle)
//        game = destination.game

//        val players = game.players
//        if (isFirstRun && players.isNullOrEmpty()) {
//            isFirstRun = false
//            viewEvent.postValue(AddPlayersClicked(game)) // Bypass home screen, just add
//        } else if (players.isNullOrEmpty())
//            viewState.postValue(ScoreBookViewState.EmptyState(game.name))
//        else if (players.isNotEmpty()) {
//            val playerEntities = mapper.map(players) { interaction ->
//                handleInteraction(interaction)
//            }
//            viewState.postValue(ScoreBookViewState.ActiveGame(playerEntities, game.name))
//        }
    }

    fun handleInteraction(interaction: ScoreBookInteraction) {
        when (interaction) {
            is PlayerClicked -> viewEvent.postValue(EditScoreForPlayer(game, interaction.player))
            is EndGameClicked -> {
                val resultText = gameEngine.endGame(game)
                viewState.postValue(ScoreBookViewState.GameOver(resultText, game.name))
                viewEvent.postValue(ScoreBookViewEvent.GoBackHome)
            }
        }
    }

    fun navigateToAddPlayerPage() =
       viewEvent.postValue(AddPlayersClicked(game))

}
