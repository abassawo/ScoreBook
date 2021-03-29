package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.shared.raw.Game

class GetClosedGames(private val games: List<Game>) {
    operator fun invoke(): List<Game> {
        return games.filter { it.isClosed }
    }
}