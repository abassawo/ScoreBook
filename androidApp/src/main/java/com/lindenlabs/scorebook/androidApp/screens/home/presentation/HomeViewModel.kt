package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameDataEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState

class HomeViewModel : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()

    init {
        val viewEntity = GameDataEntity(
            openGames = GetOpenGames().invoke(),
            closedGames = GetClosedGames().invoke()
        )
        viewState.postValue(viewEntity.toViewState())
    }

    fun GameDataEntity.toViewState() : HomeViewState {
        val listOfEntities = mutableListOf<GameRowEntity>()
        if(openGames.isNotEmpty()) {
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