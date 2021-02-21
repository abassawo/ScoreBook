package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameStrategy
import java.util.*

typealias StalematePair = Pair<Player, Player>

data class Game(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    var isClosed: Boolean = false,
    var players: List<Player> = mutableListOf(),
    val strategy: GameStrategy = GameStrategy.HighestScoreWins,
    val outcome: GameOutcome? = null)

sealed class GameOutcome {
    data class WinnerAnnounced(val player: Player) : GameOutcome()

    data class DrawAnnounced(val stalematePair: StalematePair) : GameOutcome()

//    object GameAbandoned : GameOutcome()
}