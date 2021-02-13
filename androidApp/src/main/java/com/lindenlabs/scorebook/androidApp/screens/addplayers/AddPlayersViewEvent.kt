package com.lindenlabs.scorebook.androidApp.screens.addplayers

sealed class AddPlayersViewEvent {
    data class NewPlayerAdded(val playerName: String) : AddPlayersViewEvent()
}