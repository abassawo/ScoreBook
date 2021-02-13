package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import java.util.*

typealias StalematePair = Pair<Player, Player>

data class Game(
    val id: UUID = UUID.randomUUID(),
    val isInSession: Boolean,
    val players: List<Player>,
    val outcome: GameOutcome
)

sealed class GameOutcome {
    data class WinnerAnnounced(val player: Player) : GameOutcome()

    data class DrawAnnounced(val stalematePair: StalematePair) : GameOutcome()

    object GameAbandoned : GameOutcome()
}