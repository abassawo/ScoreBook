package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

sealed class GameViewEvent {
    data class AddPlayersClicked(val game: Game) : GameViewEvent()
}