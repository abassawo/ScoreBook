package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.data.model.Player

sealed class UpdatePointsViewEvent {
    data class ScoreUpdated(val player: Player, val game: Game) : UpdatePointsViewEvent()
}