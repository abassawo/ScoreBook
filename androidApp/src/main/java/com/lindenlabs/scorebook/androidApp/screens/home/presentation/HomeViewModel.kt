package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import android.app.Application
import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.shared.common.data.UserSettingsStore
import com.lindenlabs.scorebook.shared.common.engines.home.HomeEngine
import com.lindenlabs.scorebook.shared.common.engines.home.HomeInteraction
import com.lindenlabs.scorebook.shared.common.engines.home.HomeViewEvent
import com.lindenlabs.scorebook.shared.common.engines.home.HomeViewState

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val homeEngine: HomeEngine = HomeEngine(viewModelScope, (application as ScoreBookApplication).environment, UserSettingsStore(application))
    val viewState: LiveData<HomeViewState> =
        homeEngine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<Event<HomeViewEvent>> =
        homeEngine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun handleInteraction(interaction: HomeInteraction) =
        homeEngine.handleInteraction(interaction)
}
