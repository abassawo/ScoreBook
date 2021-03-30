package com.lindenlabs.scorebook.shared.common.engines.updatepoints

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

sealed class UpdatePointsInteraction {
    data class ScoreIncreaseBy(val point: String) : UpdatePointsInteraction()
    data class ScoreLoweredBy(val point: String) : UpdatePointsInteraction()
}

sealed class UpdatePointsViewState {
    object Loading : UpdatePointsViewState()
    data class ScreenOpened(val game: Game, val player: Player) : UpdatePointsViewState()
}

sealed class UpdatePointsViewEvent {
    object Nil : UpdatePointsViewEvent()

    data class AlertNoTextEntered(val errorText: String = "Enter a score") : UpdatePointsViewEvent()

    data class ScoreUpdated(val player: Player, val game: Game) : UpdatePointsViewEvent()
}