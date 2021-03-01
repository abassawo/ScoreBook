package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.AppData
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
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
    private val gameEngine = GameEngine()
    private lateinit var appData: AppData
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()


    fun launch(appData: AppData) {
        this.appData = appData
        refresh()
    }

    fun refresh() = showGames()

    private fun showGames() {
        val viewEntity = GamesWrapper(
            openGames = GetOpenGames(appData.gameDataSource).invoke(),
            closedGames = GetClosedGames(appData.gameDataSource).invoke()
        )
        viewState.postValue(viewEntity.toViewState())
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
            appData.gameDataSource.storeGame(game)
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