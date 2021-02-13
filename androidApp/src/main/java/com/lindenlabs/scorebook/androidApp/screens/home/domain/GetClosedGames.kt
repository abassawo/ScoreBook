package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

class GetClosedGames(private val gameRepository: GameRepository = TestRepository()) {
    operator fun invoke(): List<Game> {
        return gameRepository.games.filter { it.isClosed }
    }
}