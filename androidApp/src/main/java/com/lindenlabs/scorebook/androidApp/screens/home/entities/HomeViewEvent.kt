package com.lindenlabs.scorebook.androidApp.screens.home.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

sealed class HomeViewEvent {

    object ShowWelcomeScreen : HomeViewEvent()

    object DismissWelcomeMessage : HomeViewEvent()

    data class AlertNoTextEntered(val errorText: String = "Enter a game name to begin") : HomeViewEvent()

    data class ShowAddPlayersScreen(val game: Game) : HomeViewEvent()

    data class ShowActiveGame(val game: Game) : HomeViewEvent()

    data class ShowClosedGame(val game: Game) : HomeViewEvent()

    class ShowUndoDeletePrompt(val game: Game, val restoreIndex: Int) : HomeViewEvent()
}