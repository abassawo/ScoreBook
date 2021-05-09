package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.utils.postEvent
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.entities.victory.VictoryInteraction
import com.lindenlabs.scorebook.shared.common.entities.victory.VictoryState
import com.lindenlabs.scorebook.shared.common.entities.victory.VictoryViewEvent
import kotlinx.coroutines.launch

class VictoryViewModel(val appRepository: AppRepository, gameId: String) : ViewModel() {
    val viewState: MutableLiveData<VictoryState> = MutableLiveData()
    val viewEvent: MutableLiveData<Event<VictoryViewEvent>> = MutableLiveData()

    init {
        launch(gameId)
    }

    fun launch(gameId: String) {
        viewModelScope.launch {
            val game = requireNotNull(appRepository.getGame(gameId))
            viewState.postValue(VictoryState(victoryText = game.end()))
            appRepository.updateGame(game)
        }
    }

    fun handleInteraction(interaction: VictoryInteraction) {
        when (interaction) {
            is VictoryInteraction.GoHome -> viewEvent.postEvent(VictoryViewEvent.GoHome)
        }
    }
}