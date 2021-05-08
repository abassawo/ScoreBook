package com.lindenlabs.scorebook.shared.common.entities.victory

data class VictoryState(val victoryText: String)

sealed class VictoryInteraction {
    object GoHome : VictoryInteraction()
}

sealed class VictoryViewEvent {
    object None : VictoryViewEvent()
    object GoHome : VictoryViewEvent()
}