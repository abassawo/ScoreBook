package com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

internal sealed class GameInteraction() {
    data class GameNameEntered(val name: String?) : GameInteraction()
    data class GameClicked(val game: Game) : GameInteraction()
}