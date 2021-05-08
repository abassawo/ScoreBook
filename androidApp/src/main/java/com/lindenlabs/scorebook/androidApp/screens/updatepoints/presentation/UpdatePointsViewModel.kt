package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.viewmodels.updatepoints.UpdatePointsViewState

class UpdatePointsViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val engine: UpdatePointsEngine = UpdatePointsEngine(viewModelScope, appRepository)
    val viewState: LiveData<UpdatePointsViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<Event<UpdatePointsViewEvent>> =
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext)


    fun launch(gameId: String, playerId: String) {
        engine.launch(gameId, playerId)
    }

    fun handleInteraction(interaction: UpdatePointsInteraction) =
        engine.handleInteraction(interaction)
}
