package com.lindenlabs.scorebook.androidApp.screens.addplayers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class AddPlayersViewModel: ViewModel() {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private val players: MutableList<Player> = mutableListOf()


    fun handleInteraction(interaction: AddPlayerInteraction) {
        val player = Player(interaction.name)
        when(interaction) {
            is AddPlayerInteraction.NewPlayer -> {
                players += player
                viewState.postValue(AddPlayersViewState.FinishAddPlayers(player))
                // navigate to Game Detail screen
            }
            is AddPlayerInteraction.AddAnotherPlayer -> {
                // stay on same screen
                players += Player(interaction.name)
                viewState.postValue(AddPlayersViewState.AddAnother(player))
            }
        }
    }
}