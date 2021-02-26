package com.lindenlabs.scorebook.androidApp.screens.addplayers

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

sealed class AddPlayersViewEvent {
    data class NavigateToGameDetail(val game: Game) : AddPlayersViewEvent()
}