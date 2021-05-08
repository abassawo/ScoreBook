package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.domain.GamesMapper
import com.lindenlabs.scorebook.shared.common.domain.GamesWrapper
import com.lindenlabs.scorebook.shared.common.domain.UserSettings
import com.lindenlabs.scorebook.shared.common.entities.home.GameRowEntity
import com.lindenlabs.scorebook.shared.common.entities.home.HomeInteraction
import com.lindenlabs.scorebook.shared.common.entities.home.HomeViewEvent
import com.lindenlabs.scorebook.shared.common.entities.home.HomeViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(val appRepository: AppRepository, private val userSettingsStore: UserSettings) :
    ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<Event<HomeViewEvent>> = MutableLiveData()
    private val gamesMapper: GamesMapper = GamesMapper()
    private val games: MutableList<Game> = mutableListOf()

    init {
        if (userSettingsStore.isFirstRun())
            showWelcomeScreen()

        loadGames()
        userSettingsStore.clearFirstRun()
    }

    private fun showWelcomeScreen() = viewEvent.postValue(Event(HomeViewEvent.ShowWelcomeScreen))

    private fun loadGames() = viewModelScope.launch {
        runCatching { appRepository.loadGames() }
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

    internal fun handleInteraction(interaction: HomeInteraction) = when (interaction) {
        is HomeInteraction.GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty())
                showError()
            else {
                val strategy =
                    if (interaction.lowestScoreWins) GameStrategy.LowestScoreWins else GameStrategy.HighestScoreWins
                onNewGameCreated(interaction.name!!, strategy)
            }
        }
        is HomeInteraction.GameClicked -> onGameClicked(interaction.game)
        is HomeInteraction.SwipeToDelete -> viewModelScope.launch {
            runCatching { deleteGame(interaction.game) }
                .onSuccess {
                    loadGames()
                    viewEvent.postValue(Event(HomeViewEvent.ShowUndoDeletePrompt(interaction.game, it)))
                }
                .onFailure { Timber.e(it) }
        }
        is HomeInteraction.UndoDelete -> restoreDeletedGame(interaction)
        HomeInteraction.DismissWelcome -> viewEvent.postValue(Event(HomeViewEvent.DismissWelcomeMessage))
        HomeInteraction.Refresh -> loadGames()
    }

    private fun onGameClicked(game: Game) =
            viewEvent.postValue(Event(HomeViewEvent.ShowGameDetail(game)))

    private fun restoreDeletedGame(interaction: HomeInteraction.UndoDelete) {
        games.add(interaction.restoreIndex, interaction.game)

        viewModelScope.launch {
            runCatching { appRepository.storeGame(interaction.game) }
                .onSuccess { loadGames() }
                .onFailure { Timber.e( "error trying to re-add game") }
        }
    }

    private suspend fun deleteGame(game: Game): Int {
        val index = games.indexOf(game)
        appRepository.deleteGame(game)
        return index
    }

    private fun showError() = viewEvent.postValue(Event(HomeViewEvent.AlertNoTextEntered()))

    private fun onNewGameCreated(
        name: String,
        strategy: GameStrategy,
        autoStart: Boolean = true
    ): Game {
        with(receiver = Game(name = name, strategy = strategy)) {
            if (autoStart) start()
            storeGame(this, autoStart)
            return this
        }
    }

    private fun storeGame(game: Game, autoStart: Boolean) {
        viewModelScope.launch {
            kotlin.runCatching { appRepository.storeGame(game) }
                .onSuccess {
                    if (autoStart) {
                        viewEvent.postValue(Event(HomeViewEvent.ShowAddPlayersScreen(game)))
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

    private fun Game.onClicked() = handleInteraction(HomeInteraction.GameClicked(this))

    private fun Game.onSwiped() = handleInteraction(HomeInteraction.SwipeToDelete(this))
}