package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayerInteraction
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersEngine
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersViewEvent
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersViewState

class AddPlayersViewModel(val gameId: String, val appRepository: AppRepository) : ViewModel() {
    private val engine: AddPlayersEngine = AddPlayersEngine(viewModelScope, appRepository)
    val viewState: LiveData<AddPlayersViewState> = engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<AddPlayersViewEvent> = engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    init {
        engine.launch(gameId)
    }

    fun handleInteraction(interaction: AddPlayerInteraction) =
        engine.handleInteraction(interaction)
}


