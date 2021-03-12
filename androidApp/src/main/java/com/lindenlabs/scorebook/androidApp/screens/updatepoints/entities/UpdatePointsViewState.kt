package com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class UpdatePointsViewState {
    data class ScreenOpened(val game: Game, val player: Player) : UpdatePointsViewState()
}