package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailEngine
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewEvent
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState
import javax.inject.Inject

class GameViewModel @Inject constructor(val gameId: String, appRepository: AppRepository) : ViewModel() {
    private val engine: GameDetailEngine = GameDetailEngine(viewModelScope, appRepository)
    val viewState: LiveData <GameDetailViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<Event<GameDetailViewEvent>> =
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    init {
        engine.launch(gameId)
    }

    fun handleInteraction(interaction: GameDetailInteraction) =
        engine.handleInteraction(interaction)
}