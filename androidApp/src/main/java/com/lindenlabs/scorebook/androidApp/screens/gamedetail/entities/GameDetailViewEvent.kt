package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class GameDetailViewEvent {

    data class AddPlayersClicked(val game: Game) : GameDetailViewEvent()

    data class EditScoreForPlayer(val game: Game, val player: Player) : GameDetailViewEvent()

    data class PromptToRestartGame(val game: Game) : GameDetailViewEvent()

    object GoBackHome : GameDetailViewEvent()

    object ConfirmEndGame : GameDetailViewEvent()

    data class EndGame(val game: Game) : GameDetailViewEvent()

    data class ShowRestartingGameMessage(val game: Game) : GameDetailViewEvent()

    data class NavigateToEditHome(val game: Game) : GameDetailViewEvent()

}