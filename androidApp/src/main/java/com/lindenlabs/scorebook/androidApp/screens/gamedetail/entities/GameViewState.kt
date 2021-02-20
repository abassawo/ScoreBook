package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.GameOutcome

sealed class GameViewState(open val gameName: String) {

    data class EmptyState(override val gameName: String) : GameViewState(gameName)

    data class ActiveGame(val players: List<PlayerEntity>, override val gameName: String) :
        GameViewState(gameName)

    data class GameOver(val result: String, override val gameName: String) :
        GameViewState(gameName)
}