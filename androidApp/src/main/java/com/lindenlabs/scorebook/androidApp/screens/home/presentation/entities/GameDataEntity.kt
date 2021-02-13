package com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

data class GameDataEntity(val openGames: List<Game>, val closedGames: List<Game>)