package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import com.lindenlabs.scorebook.shared.raw.Game

data class GamesWrapper(val openGames: List<Game>, val closedGames: List<Game>)