package com.lindenlabs.scorebook.androidApp.screens.addplayers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction

class AddPlayersViewModel: ViewModel() {
//    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()


    fun handleInteraction(interaction: AddPlayerInteraction) {

    }
}