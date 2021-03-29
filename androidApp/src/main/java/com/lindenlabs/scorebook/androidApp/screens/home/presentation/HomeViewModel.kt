package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.engines.home.HomeEngine
import com.lindenlabs.scorebook.shared.common.engines.home.entities.HomeInteraction
import com.lindenlabs.scorebook.shared.common.engines.home.entities.HomeViewEvent
import com.lindenlabs.scorebook.shared.common.engines.home.entities.HomeViewState

class HomeViewModel : ViewModel() {

    private val homeEngine: HomeEngine = HomeEngine(viewModelScope)

    val viewState: LiveData<HomeViewState> =
        homeEngine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<HomeViewEvent> =
        homeEngine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun handleInteraction(interaction: HomeInteraction) =
        homeEngine.handleInteraction(interaction)

}
