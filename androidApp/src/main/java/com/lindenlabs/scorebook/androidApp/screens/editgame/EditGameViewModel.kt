package com.lindenlabs.scorebook.androidApp.screens.editgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameEngine
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameInteraction
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameViewEvent
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameViewState

class EditGameViewModel : ViewModel() {
    private val engine = EditGameEngine(viewModelScope)
    val viewState: LiveData<EditGameViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<EditGameViewEvent> =
       engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun launch(gameId: String) = engine.launch(gameId)

    fun handleInteraction(interaction: EditGameInteraction) = engine.handleInteraction(interaction)
}
