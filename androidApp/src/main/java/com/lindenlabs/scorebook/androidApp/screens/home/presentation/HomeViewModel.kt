package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GamesWrapper
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState

internal class HomeViewModel : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()

    private val repository = GameRepository()

    init {
        showGames()
    }

    private fun showGames() {
        val viewEntity = GamesWrapper(
            openGames = GetOpenGames(repository).invoke(),
            closedGames = GetClosedGames(repository).invoke()
        )
        viewState.postValue(viewEntity.toViewState())
    }

    internal fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is GameNameEntered -> {
            if (interaction.name.isNullOrEmpty()) {
                showError()
            } else {
                storeNewGame(interaction.name)
            }
        }
        is GameClicked -> viewEvent.postValue(ShowGameDetail(interaction.game))
    }

    private fun showError() = viewEvent.postValue(AlertNoTextEntered())

    private fun storeNewGame(name: String) {
        val game = Game(name = name)
        repository.storeGame(game)
        viewEvent.postValue(ShowGameDetail(game))
        showGames()
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