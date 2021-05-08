package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailViewEvent
import com.lindenlabs.scorebook.shared.common.viewmodels.gamedetail.GameDetailViewState
import javax.inject.Inject

class GameViewModel @Inject constructor(appRepository: AppRepository) : ViewModel() {
    private var gameId: String? = null
    private val engine: GameDetailEngine = GameDetailEngine(viewModelScope, appRepository)
    val viewState: LiveData <GameDetailViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<Event<GameDetailViewEvent>> =
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext)


    fun launch(gameId: String) {
        this.gameId = gameId
        engine.launch(gameId)
    }

    fun refresh() = this.gameId?.let { launch(it) }

    fun handleInteraction(interaction: GameDetailInteraction) =
        engine.handleInteraction(interaction)
}