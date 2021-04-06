package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsEngine
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsViewState

class UpdatePointsViewModel(private val appRepository: AppRepository, private val gameId: String, private val playerId: String) : ViewModel() {
    private val engine: UpdatePointsEngine = UpdatePointsEngine(viewModelScope, appRepository)
    val viewState: LiveData<UpdatePointsViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<UpdatePointsViewEvent> =
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    init {
        engine.launch(gameId, playerId)
    }

    fun handleInteraction(interaction: UpdatePointsInteraction) =
        engine.handleInteraction(interaction)
}
