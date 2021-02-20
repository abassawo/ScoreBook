package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class GameViewEvent {
    data class AddPlayersClicked(val game: Game) : GameViewEvent()
    data class EditScoreForPlayer(val game: Game, val player: Player) : GameViewEvent()
}