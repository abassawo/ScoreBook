package com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

data class GamesWrapper(val openGames: List<Game>, val closedGames: List<Game>)