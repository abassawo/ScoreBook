package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryState
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryViewEvent

class VictoryViewModel(appRepository: AppRepository) : ViewModel() {
    private val victoryEngine: VictoryEngine = VictoryEngine(viewModelScope, appRepository)
    val viewState: LiveData<VictoryState> =
        victoryEngine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<VictoryViewEvent> =
        victoryEngine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun launch(gameId: String) {
        victoryEngine.launch(gameId)
    }

    fun handleInteraction(interaction: VictoryInteraction) =
        victoryEngine.handleInteraction(interaction)

}
