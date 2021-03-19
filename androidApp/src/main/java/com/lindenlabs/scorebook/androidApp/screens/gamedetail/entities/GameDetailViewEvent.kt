package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class GameDetailViewEvent {

    data class AddPlayersClicked(val game: Game) : GameDetailViewEvent()

    data class EditScoreForPlayer(val game: Game, val player: Player) : GameDetailViewEvent()

    object GoBackHome : GameDetailViewEvent()

    data class EndGame(val game: Game) : GameDetailViewEvent()

    data class ShowRestartingGameMessage(val game: Game) : GameDetailViewEvent()

}