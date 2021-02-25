package com.lindenlabs.scorebook.androidApp.screens.home.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

data class GamesWrapper(val openGames: List<Game>, val closedGames: List<Game>)