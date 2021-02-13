package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameDataEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState

internal class HomeViewModel : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<HomeViewEvent> = MutableLiveData()

    private val repository = GameRepository()

    init {
        showGames()
    }

    private fun showGames() {
        val viewEntity = GameDataEntity(
            openGames = GetOpenGames(repository).invoke(),
            closedGames = GetClosedGames(repository).invoke()
        )
        viewState.postValue(viewEntity.toViewState())
    }

    fun handleInteraction(interaction: GameInteraction) = when (interaction) {
        is NewGameClicked -> {
            if (interaction.name.isNullOrEmpty()) {
                showError()
            } else {
                storeNewGame(interaction.name)
            }
        }
    }

    private fun showError() = viewEvent.postValue(HomeViewEvent.AlertNoTextEntered)

    private fun storeNewGame(name: String) {
        repository.storeGame(name)
        showGames()
    }

    private fun GameDataEntity.toViewState(): HomeViewState {
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
}

private fun List<Game>.toBodyEntity(): List<GameRowEntity.BodyType> {
    return this.map { GameRowEntity.BodyType(it) }
}

sealed class GameRowEntity {
    data class HeaderType(val title: String) : GameRowEntity()

    data class BodyType(val game: Game) : GameRowEntity()
}