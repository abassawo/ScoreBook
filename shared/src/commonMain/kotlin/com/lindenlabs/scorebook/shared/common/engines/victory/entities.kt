package com.lindenlabs.scorebook.shared.common.engines.victory

import com.lindenlabs.scorebook.shared.common.engines.BaseInteraction

data class VictoryState(val victoryText: String)

sealed class VictoryInteraction : BaseInteraction {
    object GoHome : VictoryInteraction()
}

sealed class VictoryViewEvent {
    object None : VictoryViewEvent()
    object GoHome : VictoryViewEvent()
}