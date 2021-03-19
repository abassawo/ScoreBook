package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.victory.entities.VictoryInteraction
import com.lindenlabs.scorebook.androidApp.screens.victory.entities.VictoryState
import com.lindenlabs.scorebook.androidApp.screens.victory.entities.VictoryViewEvent
import kotlinx.coroutines.launch

class VictoryViewModel(val appRepository: AppRepository, args: VictoryFragmentArgs) : ViewModel() {
    val viewState: MutableLiveData<VictoryState> = MutableLiveData()
    val viewEvent: MutableLiveData<VictoryViewEvent> = MutableLiveData()


    init {
        viewState.postValue(VictoryState(victoryText = args.gameArg.end()))
        viewModelScope.launch {
            appRepository.updateGame(args.gameArg)
        }
    }

    fun handleInteraction(interaction: VictoryInteraction) {
        when (interaction) {
            VictoryInteraction.GoHome -> viewEvent.postValue(VictoryViewEvent.GoHome)
        }
    }
}