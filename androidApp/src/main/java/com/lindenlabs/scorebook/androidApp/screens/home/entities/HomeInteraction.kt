package com.lindenlabs.scorebook.androidApp.screens.home.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

sealed class HomeInteraction {
    data class GameDetailsEntered(val name: String?, val lowestScoreWins: Boolean = false) : HomeInteraction()
    data class GameClicked(val game: Game) : HomeInteraction()
    data class SwipeToDelete(val game: Game) : HomeInteraction()
    data class UndoDelete(val game: Game, val restoreIndex: Int) : HomeInteraction()
    object DismissWelcome : HomeInteraction()
}