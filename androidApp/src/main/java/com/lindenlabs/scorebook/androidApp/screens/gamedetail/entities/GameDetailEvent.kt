package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class GameDetailEvent {

    sealed class ActiveGame : GameDetailEvent() {

        data class AddPlayersClicked(val game: Game) :ActiveGame()

        data class EditScoreForPlayer(val game: Game, val player: Player) :ActiveGame()

        object GoBackHome : ActiveGame()

        data class EndGame(val game: Game) :ActiveGame()
    }

    sealed class ClosedGame : GameDetailEvent() {
        object GoBackHome : ClosedGame()

        data class RestartGame(val game: Game) : ClosedGame()
    }
}