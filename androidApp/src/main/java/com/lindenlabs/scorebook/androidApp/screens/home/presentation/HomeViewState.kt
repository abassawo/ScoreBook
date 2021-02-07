package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import com.lindenlabs.scorebook.androidApp.screens.home.data.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameData.*

sealed class HomeViewState {
    object EmptyState : HomeViewState()
    data class GamesState(val openGames: OpenGames, val closedGames: ClosedGames) : HomeViewState()
}

sealed class GameData(open val games: List<Game>) {
    data class OpenGames(override val games: List<Game>) : GameData(games)
    data class ClosedGames(override val games: List<Game>) : GameData(games)
}

