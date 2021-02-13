package com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

sealed class HomeViewEvent {
    data class AlertNoTextEntered(val errorText: String = "Enter a game name to begin") : HomeViewEvent()

    data class ShowGameDetail(val game: Game) : HomeViewEvent()
}