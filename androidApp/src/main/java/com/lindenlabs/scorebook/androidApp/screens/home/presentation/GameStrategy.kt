package com.lindenlabs.scorebook.androidApp.screens.home.presentation

sealed class GameStrategy {
    object HighestScoreWins : GameStrategy()
    object LowestScoreWins : GameStrategy()
}