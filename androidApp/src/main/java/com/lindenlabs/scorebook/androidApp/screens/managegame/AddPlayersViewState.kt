package com.lindenlabs.scorebook.androidApp.screens.managegame

sealed class AddPlayersViewState {
    data class UpdateCurrentPlayersText(val playersText: String) : AddPlayersViewState()
    object TextEntryError : AddPlayersViewState()
}