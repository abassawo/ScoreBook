package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.GameStrategy
import com.lindenlabs.scorebook.androidApp.screens.home.entities.*
import com.lindenlabs.scorebook.androidApp.base.data.raw.GameStrategy.HighestScoreWins
import com.lindenlabs.scorebook.androidApp.base.data.raw.GameStrategy.LowestScoreWins
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GamesMapper
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GamesWrapper
import kotlinx.coroutines.launch

class HomeViewModel(val appRepository: AppRepository) : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()
    private val gamesMapper: GamesMapper = GamesMapper()

    init {
        loadGames()
    }

    private fun loadGames() = viewModelScope.launch {
        runCatching { appRepository.load() }
            .onSuccess(::showGames)
            .onFailure { }
    }


    private fun showGames(games: List<Game>) {
        val wrapper = gamesMapper.mapGamesToWrapper(games)
        viewState.postValue(wrapper.toViewState())
    }

    internal fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty())
                showError()
            else {
                val strategy = if (interaction.lowestScoreWins) LowestScoreWins else HighestScoreWins
                startNewGame(interaction.name, strategy)
            }
        }
        is GameClicked -> viewEvent.postValue(ShowActiveGame(interaction.game))
        is SwipeToDelete -> viewModelScope.launch {
            runCatching { deleteGame(interaction.game) }
                .onSuccess { loadGames() }
                .onFailure { }
        }
        is UndoDelete -> storeGame(interaction.game)
    }


    private suspend fun deleteGame(game: Game) {
        appRepository.deleteGame(game)
        viewEvent.postValue(ShowUndoDeletePrompt(game))
    }

    private fun showError() = viewEvent.postValue(AlertNoTextEntered())

    private fun startNewGame(name: String, strategy: GameStrategy): Game {
        return Game(name = name, strategy = strategy).also { game ->
            game.start()
            storeGame(game)
        }
    }

    private fun storeGame(game: Game) = viewModelScope.launch {
        kotlin.runCatching { appRepository.storeGame(game) }
            .onSuccess { viewEvent.postValue(ShowAddPlayersScreen(game)) }
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
        this.map { game -> game.toBodyRow() }

    private fun Game.toBodyRow() =
        GameRowEntity.BodyType(
            game = this,
            clickAction = { this.onClicked() },
            swipeAction = { this.onSwiped() })

    private fun Game.onClicked() = handleInteraction(GameClicked(this))

    private fun Game.onSwiped() = handleInteraction(SwipeToDelete(this))
}