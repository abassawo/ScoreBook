package com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

sealed class UpdatePointsViewEvent {
    data class AlertNoTextEntered(val errorText: String = "Enter a score") : UpdatePointsViewEvent()

    data class ScoreUpdated(val player: Player, val game: Game) : UpdatePointsViewEvent()
}