package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities

import com.lindenlabs.scorebook.androidApp.data.model.Player

sealed class ScoreBookInteraction {
    data class PlayerClicked(val player : Player) : ScoreBookInteraction()
    object EndGameClicked : ScoreBookInteraction()
}