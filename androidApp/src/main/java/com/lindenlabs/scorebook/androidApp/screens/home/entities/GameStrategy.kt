package com.lindenlabs.scorebook.androidApp.screens.home.entities

sealed class GameStrategy {
    object HighestScoreWins : GameStrategy()
    object LowestScoreWins : GameStrategy()
}