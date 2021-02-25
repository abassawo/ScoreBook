package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.data.model.Player

sealed class UpdatePointsViewState {
    data class ScreenOpened(val game: Game, val player: Player) : UpdatePointsViewState()
}