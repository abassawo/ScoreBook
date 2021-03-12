package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.Launchable
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.GameClicked
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.GameDetailsEntered
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameStrategy.HighestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameStrategy.LowestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(val environment: Environment) : ViewModel(), Launchable {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()
    private val gamesMapper: GamesMapper = GamesMapper()
//    private val workerScope = environment.coroutineScope ?: viewModelScope

    init {
        launch()
    }

    override fun launch() {
        viewModelScope.launch {
                withContext(environment.dispatcher) {
                runCatching { environment.load() }
                     .onSuccess(::showGames)
                    .onFailure { }
            }
        }
    }

    private fun showGames(games: List<Game>) {
        val wrapper = gamesMapper.mapGamesToWrapper(games)
        viewState.postValue(wrapper.toViewState())
    }

    internal fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty()) {
                showError()
            } else {
                val strategy =
                    if (interaction.lowestScoreWins) LowestScoreWins else HighestScoreWins
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
                withContext(environment.dispatcher) {
                    environment.storeGame(game)
                }
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

    private fun List<Game>.toBodyEntity(): List<GameRowEntity.BodyType> =
        this.map { game -> GameRowEntity.BodyType(game) { handleInteraction(GameClicked(game)) } }
}
