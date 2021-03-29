package com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

sealed class UpdatePointsViewState {
    data class ScreenOpened(val game: Game, val player: Player) : UpdatePointsViewState()
}