package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import com.lindenlabs.scorebook.androidApp.di.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.GameClicked
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.GameDetailsEntered
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameStrategy.HighestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameStrategy.LowestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()
    private lateinit var gameDataSource: GameDataSource

    fun launch(appRepository: AppRepository) {
        this.gameDataSource = appRepository.gameDataSource
        refresh()
    }

    private fun refresh() = showGames()

    private fun showGames() {
        gameDataSource.load { pairOfOpenToClosedGames ->
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
            game.start()
            viewModelScope.launch {
                gameDataSource.storeGame(game)
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


sealed class GameRowEntity {
    data class HeaderType(val title: String) : GameRowEntity()

    data class BodyType(val game: Game, val clickAction: (interaction: GameInteraction) -> Unit) : GameRowEntity()
}