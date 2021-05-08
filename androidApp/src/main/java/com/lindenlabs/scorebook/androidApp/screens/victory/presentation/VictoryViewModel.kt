package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.data.AppRepository
<<<<<<< HEAD
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryEngine
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryInteraction
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryState
import com.lindenlabs.scorebook.shared.common.viewmodels.victory.VictoryViewEvent
=======
import com.lindenlabs.scorebook.shared.common.entities.victory.VictoryInteraction
import com.lindenlabs.scorebook.shared.common.entities.victory.VictoryState
import com.lindenlabs.scorebook.shared.common.entities.victory.VictoryViewEvent
import kotlinx.coroutines.launch
>>>>>>> Use passed in constructor values for viewmodel

class VictoryViewModel(val appRepository: AppRepository, gameId: String) : ViewModel() {
    val viewState: MutableLiveData<VictoryState> =  MutableLiveData()
    val viewEvent: MutableLiveData<VictoryViewEvent> = MutableLiveData()

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
            is VictoryInteraction.GoHome -> Unit // viewEvent.value = VictoryViewEvent.GoHome
        }
    }
}