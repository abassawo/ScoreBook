package com.lindenlabs.scorebook.androidApp.screens.addplayers

sealed class AddPlayersViewState {
    data class UpdateCurrentPlayersText(val playersText: String) : AddPlayersViewState()
    data class ValidateTextForPlusButton(val isEnabled: Boolean): AddPlayersViewState()
    object TextEntryError : AddPlayersViewState()
    object TypingState : AddPlayersViewState()
}