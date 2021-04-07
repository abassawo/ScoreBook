package com.lindenlabs.scorebook.androidApp.navigation

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

sealed class Destination {
    object Home : Destination()
    data class AddPlayers(val game: Game) : Destination()
    data class EditGame(val game: Game) : Destination()
    data class GameDetail(val game: Game) : Destination()
    data class UpdatePoints(val game: Game, val player: Player) : Destination()
    data class VictoryScreen(val game: Game) : Destination()
}