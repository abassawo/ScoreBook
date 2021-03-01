package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class UpdatePointsViewEvent {
    data class ScoreUpdated(val player: Player, val game: Game) : UpdatePointsViewEvent()
}