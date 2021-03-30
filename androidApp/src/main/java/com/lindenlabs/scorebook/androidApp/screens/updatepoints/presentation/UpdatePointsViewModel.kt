package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsEngine
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsViewState

class UpdatePointsViewModel(val gameId: String, val playerId: String) : ViewModel() {
    private val engine: UpdatePointsEngine = UpdatePointsEngine(viewModelScope)

    val viewState: LiveData<UpdatePointsViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<UpdatePointsViewEvent> =
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun launch(gameId: String, playerId: String) =
        engine.launch(gameId, playerId)

    fun handleInteraction(interaction: UpdatePointsInteraction) =
        engine.handleInteraction(interaction)
}
