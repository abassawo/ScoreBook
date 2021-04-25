package com.lindenlabs.scorebook.shared.common.viewmodels.victory

import com.lindenlabs.scorebook.shared.common.viewmodels.BaseInteraction

data class VictoryState(val victoryText: String)

sealed class VictoryInteraction : BaseInteraction {
    object GoHome : VictoryInteraction()
}

sealed class VictoryViewEvent {
    object None : VictoryViewEvent()
    object GoHome : VictoryViewEvent()
}