package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.engines.victory.VictoryEngine
import com.lindenlabs.scorebook.shared.common.engines.victory.VictoryInteraction
import com.lindenlabs.scorebook.shared.common.engines.victory.VictoryState
import com.lindenlabs.scorebook.shared.common.engines.victory.VictoryViewEvent

class VictoryViewModel(val gameId: String, appRepository: AppRepository) : ViewModel() {
    private val victoryEngine: VictoryEngine = VictoryEngine(viewModelScope, appRepository)
    val viewState: LiveData<VictoryState> =
        victoryEngine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<VictoryViewEvent> =
        victoryEngine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    init {
        victoryEngine.launch(gameId)
    }

    fun handleInteraction(interaction: VictoryInteraction) =
        victoryEngine.handleInteraction(interaction)

}
