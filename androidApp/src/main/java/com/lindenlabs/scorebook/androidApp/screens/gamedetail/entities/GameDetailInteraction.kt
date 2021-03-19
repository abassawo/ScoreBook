package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

sealed class GameDetailInteraction {
    data class PlayerClicked(val player : Player) : GameDetailInteraction()
    object GoBack : GameDetailInteraction()
    object EndGameClicked : GameDetailInteraction()
    object EndGameConfirmed : GameDetailInteraction()
    object RestartGameClicked : GameDetailInteraction()
}