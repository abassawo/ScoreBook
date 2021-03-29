package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.shared.raw.Game

class GetOpenGames(private val games: List<Game>) {
    operator fun invoke() = games.filterNot { it.isClosed }

}