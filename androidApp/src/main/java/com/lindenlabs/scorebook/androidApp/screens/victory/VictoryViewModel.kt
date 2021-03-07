package com.lindenlabs.scorebook.androidApp.screens.victory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Environment
import kotlinx.coroutines.launch

data class VictoryState(val victoryText: String)

class VictoryViewModel : ViewModel() {
    val viewState: MutableLiveData<VictoryState> = MutableLiveData()
    val viewEvent: MutableLiveData<VictoryViewEvent> = MutableLiveData()

    fun launch(environment: Environment, args: VictoryFragmentArgs) {
        viewState.postValue(VictoryState(victoryText = args.gameArg.end()))
        viewModelScope.launch {
            environment.updateGame(args.gameArg)
        }
    }
}