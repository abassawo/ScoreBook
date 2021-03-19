package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class ScoreBookInteraction {
    data class PlayerClicked(val player : Player) : ScoreBookInteraction()
    object GoBack : ScoreBookInteraction()
    object EndGameClicked : ScoreBookInteraction()
    object RestartGameClicked : ScoreBookInteraction()
}