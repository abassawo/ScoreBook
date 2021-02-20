package com.lindenlabs.scorebook.androidApp.screens.addpoints

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class AddPointsViewEvent {
    data class ScoreUpdated(val player: Player, val game: Game) : AddPointsViewEvent()
}