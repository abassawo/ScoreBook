package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

sealed class ScoreBookViewState(open val gameName: String) {

    data class EmptyState(override val gameName: String) : ScoreBookViewState(gameName)

    data class ActiveGame(val scoreBooks: List<ScoreBookEntity>, override val gameName: String) :
        ScoreBookViewState(gameName)
}