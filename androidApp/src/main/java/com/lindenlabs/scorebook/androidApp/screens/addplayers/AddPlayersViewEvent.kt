package com.lindenlabs.scorebook.androidApp.screens.addplayers

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

sealed class AddPlayersViewEvent {
    data class NavigateToGameDetail(val game: Game) : AddPlayersViewEvent()
    object NavigateHome : AddPlayersViewEvent()
}