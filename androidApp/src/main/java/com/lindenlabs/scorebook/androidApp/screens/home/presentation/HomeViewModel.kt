package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.domain.IsFirstRun

class HomeViewModel : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()
//    val viewEvent: SingleLiveEvent<HomeViewState> = SingleLiveEvent()

    init {
        if(IsFirstRun()) {
            viewState.postValue(HomeViewState.InitialState)
        }
    }
}