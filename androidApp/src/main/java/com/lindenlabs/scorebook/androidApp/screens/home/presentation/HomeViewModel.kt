package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GamesWrapper
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState

internal class HomeViewModel : ViewModel() {
    private lateinit var repository: GameDataSource
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()
    private val gameEngine: GameEngine = GameEngine()

    fun launch(appNavigator: AppNavigator) {
        this.repository = appNavigator.gamesDataSource
        refresh()
    }

    fun refresh() = showGames()


    private fun showGames() =
        repository.getAllGames { games ->
            val gamesWrapper = GamesWrapper(
                openGames = games.filter { !it.isClosed },
                closedGames = games.filter { it.isClosed })
            viewState.postValue(gamesWrapper.toViewState())
        }

    internal fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty()) {
                showError()
            } else {
                val strategy =
                    if (interaction.lowestScoreWins) LowestScoreWins else HighestScoreWins
                storeNewGame(interaction.name, strategy)
            }
        }
        is GameClicked -> viewEvent.postValue(ShowGameDetail(interaction.game))
    }

    private fun showError() = viewEvent.postValue(AlertNoTextEntered())

    private fun storeNewGame(name: String, strategy: GameStrategy) {
        val game = Game(name = name, strategy = strategy)
        repository.storeGame(game)
        gameEngine.startGame(game)
        viewEvent.postValue(ShowGameDetail(game))
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

    data class BodyType(val game: Game, val clickAction: (interaction: GameInteraction) -> Unit) :
        GameRowEntity()
}