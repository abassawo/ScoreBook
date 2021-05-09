package com.lindenlabs.scorebook.shared.common.entities.updatepoints

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

sealed class UpdatePointsInteraction(open val point: String) {
    data class ScoreIncreaseBy(override val point: String) : UpdatePointsInteraction(point)
    data class ScoreLoweredBy(override val point: String) : UpdatePointsInteraction(point)
}

sealed class UpdatePointsViewState {
    object Loading : UpdatePointsViewState()
    data class ScreenOpened(val game: Game, val player: Player) : UpdatePointsViewState()
}

sealed class UpdatePointsViewEvent {
    object Loading : UpdatePointsViewEvent()

    data class AlertNoTextEntered(val errorText: String = "Enter a score") : UpdatePointsViewEvent()

    data class ScoreUpdated(val player: Player, val game: Game) : UpdatePointsViewEvent()
}