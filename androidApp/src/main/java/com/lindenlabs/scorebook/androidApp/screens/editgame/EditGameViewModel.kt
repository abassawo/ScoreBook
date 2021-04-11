package com.lindenlabs.scorebook.androidApp.screens.editgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.viewmodels.editgame.EditGameEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.editgame.EditGameInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.editgame.EditGameViewEvent
import com.lindenlabs.scorebook.shared.common.viewmodels.editgame.EditGameViewState

class EditGameViewModel(val gameId: String, val appRepository: AppRepository) : ViewModel() {
    private val engine = EditGameEngine(viewModelScope, appRepository)
    val viewState: LiveData<EditGameViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<EditGameViewEvent> =
       engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun launch(gameId: String) = engine.launch(gameId)

    fun handleInteraction(interaction: EditGameInteraction) = engine.handleInteraction(interaction)
}
