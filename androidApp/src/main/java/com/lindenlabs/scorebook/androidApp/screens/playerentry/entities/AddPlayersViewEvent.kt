package com.lindenlabs.scorebook.androidApp.screens.playerentry.entities

import com.lindenlabs.scorebook.shared.raw.Game

sealed class AddPlayersViewEvent {
    data class NavigateToGameDetail(val game: Game) : AddPlayersViewEvent()
    object NavigateHome : AddPlayersViewEvent()
}