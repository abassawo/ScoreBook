package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class ScoreBookViewEvent {
    data class AddPlayersClicked(val game: Game) : ScoreBookViewEvent()
    data class EditScoreForPlayer(val game: Game, val player: Player) : ScoreBookViewEvent()
    object GoBackHome : ScoreBookViewEvent()
}