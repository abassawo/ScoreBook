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
import com.lindenlabs.scorebook.androidApp.settings.UserSettings
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(val appRepository: AppRepository) :
    ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()
    private val gamesMapper: GamesMapper = GamesMapper()
    private val games: MutableList<Game> = mutableListOf()

    init {
        if (appRepository.isFirstRun())
            showWelcomeScreen()

        loadGames()
        appRepository.clearFirstRun()
    }

    private fun showWelcomeScreen() = viewEvent.postValue(ShowWelcomeScreen)

    private fun loadGames() = viewModelScope.launch {
        runCatching { appRepository.load() }
            .onSuccess(::showGames)
            .onFailure { }
    }

    private fun showGames(games: List<Game>) {
        this.games.run {
            clear()
            addAll(games)
        }
        val wrapper = gamesMapper.mapGamesToWrapper(games)
        viewState.postValue(wrapper.toViewState())
    }

    internal fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty())
                showError()
            else {
                val strategy =
                    if (interaction.lowestScoreWins) LowestScoreWins else HighestScoreWins
                onNewGameCreated(interaction.name, strategy, autoStart = games.isEmpty())
            }
        }
        is GameClicked -> viewEvent.postValue(ShowActiveGame(interaction.game))
        is SwipeToDelete -> viewModelScope.launch {
            runCatching { deleteGame(interaction.game) }
                .onSuccess {
                    loadGames()
                    viewEvent.postValue(ShowUndoDeletePrompt(interaction.game, it))
                }
                .onFailure { Timber.e(it) }
        }
        is UndoDelete -> restoreDeletedGame(interaction)
    }

    private fun restoreDeletedGame(interaction: UndoDelete) {
        games.add(interaction.restoreIndex, interaction.game)

        viewModelScope.launch {
            runCatching { appRepository.storeGame(interaction.restoreIndex, interaction.game) }
                .onSuccess {
                    with(interaction) {
                        appRepository.storeGame(interaction.restoreIndex, game)
                        loadGames()
                    }
                }.onFailure { Timber.e("error trying to re-add game$it") }
        }
    }

    private suspend fun deleteGame(game: Game): Int {
        val index = games.indexOf(game)
        appRepository.deleteGame(game)
        return index
    }

    private fun showError() = viewEvent.postValue(AlertNoTextEntered())

    private fun onNewGameCreated(
        name: String,
        strategy: GameStrategy,
        autoStart: Boolean = false
    ): Game {
        with(receiver = Game(name = name, strategy = strategy)) {
            if (autoStart) start()

            storeGame(this)
            return this
        }
    }

    private fun storeGame(game: Game, autoStart: Boolean = games.isEmpty()) {
        viewModelScope.launch {
            kotlin.runCatching { appRepository.storeGame(game) }
                .onSuccess {
                    if (autoStart) {
                        viewEvent.postValue(ShowAddPlayersScreen(game))
                    } else {
                        loadGames()
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
        this.map { game -> game.toBodyRow() }

    private fun Game.toBodyRow() =
        GameRowEntity.BodyType(
            game = this,
            clickAction = { this.onClicked() },
            swipeAction = { this.onSwiped() })

    private fun Game.onClicked() = handleInteraction(GameClicked(this))

    private fun Game.onSwiped() = handleInteraction(SwipeToDelete(this))
}