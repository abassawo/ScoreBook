package com.lindenlabs.scorebook.androidApp.screens.managegame

sealed class AddPlayersViewEvent {
    object NavigateToGameDetail : AddPlayersViewEvent()
}