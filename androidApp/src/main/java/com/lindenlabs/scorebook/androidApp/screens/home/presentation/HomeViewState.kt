package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import com.lindenlabs.scorebook.androidApp.screens.home.data.Game
typealias GameHistory = List<Game>

sealed class HomeViewState {
    object InitialState : HomeViewState()
    data class NoActiveGame(val history: GameHistory)
    data class ActiveGame(val gameInSession: Game, val history: GameHistory)
}


