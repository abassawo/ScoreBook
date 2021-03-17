package com.lindenlabs.scorebook.androidApp.screens.home.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

sealed class GameInteraction {
    data class GameDetailsEntered(val name: String?, val lowestScoreWins: Boolean = false) : GameInteraction()
    data class GameClicked(val game: Game) : GameInteraction()
    data class SwipeToDelete(val game: Game) : GameInteraction()
    data class UndoDelete(val game: Game, val restoreIndex: Int) : GameInteraction()
}