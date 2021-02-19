package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class GameViewState {
    object EmptyState : GameViewState()
    data class PlayersAdded(val players: List<Player>) : GameViewState()
}