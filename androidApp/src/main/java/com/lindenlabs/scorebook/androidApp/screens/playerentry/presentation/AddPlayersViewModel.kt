package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersEngine
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayersViewEvent
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayersViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import javax.inject.Inject

class AddPlayersViewModel @Inject constructor() : ViewModel() {
    private val engine: AddPlayersEngine = AddPlayersEngine(viewModelScope, Game(name="Test"))
    val viewState: LiveData<AddPlayersViewState> = engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<AddPlayersViewEvent> = engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun handleInteraction(interaction: AddPlayerInteraction) =
        engine.handleInteraction(interaction)

}