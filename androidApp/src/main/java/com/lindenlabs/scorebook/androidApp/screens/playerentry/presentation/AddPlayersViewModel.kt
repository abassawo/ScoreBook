package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.viewmodels.addplayers.AddPlayerInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.addplayers.AddPlayersEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.addplayers.AddPlayersViewEvent
import com.lindenlabs.scorebook.shared.common.viewmodels.addplayers.AddPlayersViewState

class AddPlayersViewModel(val appRepository: AppRepository) : ViewModel() {
    private val engine: AddPlayersEngine = AddPlayersEngine(viewModelScope, appRepository)
    val viewState: LiveData<AddPlayersViewState> = engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<Event<AddPlayersViewEvent>> = engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun launch(gameId: String) = engine.launch(gameId)

    fun handleInteraction(interaction: AddPlayerInteraction) =
        engine.handleInteraction(interaction)
}


