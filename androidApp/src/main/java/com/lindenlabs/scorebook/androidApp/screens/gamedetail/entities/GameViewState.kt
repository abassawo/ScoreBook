package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

sealed class GameViewState(open val gameName: String) {
    data class EmptyState(override val gameName: String) : GameViewState(gameName)
    data class PlayersAdded(val players: List<PlayerEntity>, override val gameName: String) : GameViewState(gameName)
}