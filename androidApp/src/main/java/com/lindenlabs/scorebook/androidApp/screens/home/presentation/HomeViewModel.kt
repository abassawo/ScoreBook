package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy.HighestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy.LowestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.GameClicked
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.GameDetailsEntered
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GamesWrapper
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState
import kotlinx.coroutines.launch

internal class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val gameEngine = GameEngine()
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()
    private val gamesRepo: GameDataSource = PersistentGameRepository.getInstance(application)

    init {
        refresh()
    }

    private fun refresh() = showGames()

    private fun showGames() {
        gamesRepo.load { pairOfOpenToClosedGames ->
            val (openGames, closedGames) = pairOfOpenToClosedGames
            val viewEntity = GamesWrapper(openGames, closedGames )
            viewState.postValue(viewEntity.toViewState())
        }
    }

    internal fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty()) {
                showError()
            } else {
                val strategy = if(interaction.lowestScoreWins) LowestScoreWins else HighestScoreWins
                val game = storeNewGame(interaction.name, strategy)
                viewEvent.postValue(ShowAddPlayersScreen(game))
            }
        }
        is GameClicked -> viewEvent.postValue(ShowActiveGame(interaction.game))
    }

    private fun showError() = viewEvent.postValue(AlertNoTextEntered())

    private fun storeNewGame(name: String, strategy: GameStrategy): Game {
        return Game(name = name, strategy = strategy).also { game ->
            gameEngine.startGame(game)
            viewModelScope.launch {
                gamesRepo.storeGame(game)
            }
        }

    }

    private fun GamesWrapper.toViewState(): HomeViewState {
        val listOfEntities = mutableListOf<GameRowEntity>()
        if (openGames.isNotEmpty()) {
            listOfEntities += GameRowEntity.HeaderType("Open Games:")
            listOfEntities += openGames.toBodyEntity()
        }
        if (closedGames.isNotEmpty()) {
            listOfEntities += GameRowEntity.HeaderType("Closed Games:")
            listOfEntities += closedGames.toBodyEntity()
        }
        return HomeViewState(listOfEntities)
    }

    private fun List<Game>.toBodyEntity(): List<GameRowEntity.BodyType> {
        return this.map { game -> GameRowEntity.BodyType(game) { handleInteraction(GameClicked(game)) } }
    }
}


internal sealed class GameRowEntity {
    data class HeaderType(val title: String) : GameRowEntity()

    data class BodyType(val game: Game, val clickAction: (interaction: GameInteraction) -> Unit) : GameRowEntity()
}