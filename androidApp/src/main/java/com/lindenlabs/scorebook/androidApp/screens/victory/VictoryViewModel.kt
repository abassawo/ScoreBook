
package com.lindenlabs.scorebook.androidApp.screens.victory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class VictoryState(val victoryText: String)

class VictoryViewModel(val appRepository: AppRepository, args: VictoryFragmentArgs, coroutineScope: CoroutineScope? = null) : ViewModel() {
    val viewState: MutableLiveData<VictoryState> = MutableLiveData()
    val viewEvent: MutableLiveData<VictoryViewEvent> = MutableLiveData()


    init {
        viewState.postValue(VictoryState(victoryText = args.gameArg.end()))
        viewModelScope.launch {
            appRepository.updateGame(args.gameArg)
        }
    }
}