package com.lindenlabs.scorebook.androidApp.screens.addpoints

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class AddPointsViewState {
    data class ScreenOpened(val game: Game, val player: Player) : AddPointsViewState()
}