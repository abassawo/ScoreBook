package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.engines.home.HomeEngine
import com.lindenlabs.scorebook.shared.common.engines.home.HomeInteraction
import com.lindenlabs.scorebook.shared.common.engines.home.HomeViewEvent
import com.lindenlabs.scorebook.shared.common.engines.home.HomeViewState

class HomeViewModel : ViewModel() {
    private val homeEngine: HomeEngine = HomeEngine(viewModelScope)
    val viewState: LiveData<HomeViewState> =
        homeEngine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<HomeViewEvent> =
        homeEngine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun handleInteraction(interaction: HomeInteraction) =
        homeEngine.handleInteraction(interaction)
}
