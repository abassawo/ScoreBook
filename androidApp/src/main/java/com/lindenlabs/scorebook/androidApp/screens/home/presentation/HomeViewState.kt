package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

sealed class HomeViewState {
    object EmptyState : HomeViewState()
    data class GamesState(val openGames: List<Game>, val closedGames: List<Game>) : HomeViewState()
}

