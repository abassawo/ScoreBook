package com.lindenlabs.scorebook.shared.common.engines.home

import com.lindenlabs.scorebook.shared.common.Environment
import com.lindenlabs.scorebook.shared.common.domain.GamesMapper
import com.lindenlabs.scorebook.shared.common.domain.GamesWrapper
import com.lindenlabs.scorebook.shared.common.domain.UserSettings
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeEngine(private val coroutineScope: CoroutineScope, private val environment: Environment, userSettings: UserSettings) {
    val viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState(emptyList()))
    val viewEvent: MutableStateFlow<HomeViewEvent> = MutableStateFlow(HomeViewEvent.None)
    private val gamesMapper: GamesMapper = GamesMapper()
    private val games: MutableList<Game> = mutableListOf()

    private val appRepository = environment.appRepository

    init {
        if (userSettings.isFirstRun())
            showWelcomeScreen()

        loadGames()
        userSettings.clearFirstRun()
    }

    private fun showWelcomeScreen() {
        viewEvent.value = HomeViewEvent.ShowWelcomeScreen
    }

    private fun loadGames() {
        coroutineScope.launch {
            runCatching {
                appRepository.load()
            }
                .onSuccess(::showGames)
                .onFailure { }
        }
    }

    private fun showGames(games: List<Game>) {
        this.games.run {
            clear()
            addAll(games)
        }
        val wrapper = gamesMapper.mapGamesToWrapper(games)
        viewState.value = wrapper.toViewState()
    }

    fun handleInteraction(interaction: HomeInteraction) = when (interaction) {
        is HomeInteraction.GameDetailsEntered -> {
            if (interaction.name.isNullOrEmpty())
                showError()
            else {
                val strategy =
                    if (interaction.lowestScoreWins) GameStrategy.LowestScoreWins else GameStrategy.HighestScoreWins
                onNewGameCreated(interaction.name, strategy)
            }
        }
        is HomeInteraction.GameClicked -> onGameClicked(interaction.game)
        is HomeInteraction.SwipeToDelete -> {
            runCatching { deleteGame(interaction.game) }
                .onSuccess {
                    loadGames()
                    viewEvent.value = HomeViewEvent.ShowUndoDeletePrompt(interaction.game, it)
                }
                .onFailure {
//                    Timber.e(it)
                }
        }
        is HomeInteraction.UndoDelete -> restoreDeletedGame(interaction)
        HomeInteraction.DismissWelcome -> viewEvent.value = HomeViewEvent.DismissWelcomeMessage
    }

    private fun onGameClicked(game: Game) {
        viewEvent.value = HomeViewEvent.ShowGameDetail(game)
    }

    private fun restoreDeletedGame(interaction: HomeInteraction.UndoDelete) {
        games.add(interaction.restoreIndex, interaction.game)

        coroutineScope.launch {
            runCatching {
                appRepository.storeGame(interaction.game)
            }
                .onSuccess { loadGames() }
                .onFailure {
//                    Timber.e("error trying to re-add game$it")
                }
        }
    }

    private fun deleteGame(game: Game): Int {
        val index = games.indexOf(game)
        coroutineScope.launch {
            appRepository.deleteGame(game)
        }
        return index
    }

    private fun showError() {
        viewEvent.value = HomeViewEvent.AlertNoTextEntered()
    }

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
        coroutineScope.launch {
            kotlin.runCatching {
                appRepository.storeGame(game)
            }
                .onSuccess {
                    if (autoStart) {
                        viewEvent.value = HomeViewEvent.ShowAddPlayersScreen(game)
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