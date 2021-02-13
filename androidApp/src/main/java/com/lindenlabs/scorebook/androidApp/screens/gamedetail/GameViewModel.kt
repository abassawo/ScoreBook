package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState

class GameViewModel : ViewModel() {
    val viewState: MutableLiveData<GameViewState> = MutableLiveData()
//    val viewEvent: MutableLiveData<GameViewEvent> = MutableLiveData()
}