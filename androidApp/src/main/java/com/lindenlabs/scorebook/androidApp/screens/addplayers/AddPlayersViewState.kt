package com.lindenlabs.scorebook.androidApp.screens.addplayers

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class AddPlayersViewState(open val playerToAdd: Player) {
    data class FinishAddPlayers(override val playerToAdd: Player) : AddPlayersViewState(playerToAdd)
    data class AddAnother(override val playerToAdd: Player) : AddPlayersViewState(playerToAdd)

}