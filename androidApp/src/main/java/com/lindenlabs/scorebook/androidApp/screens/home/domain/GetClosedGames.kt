package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

class GetClosedGames(private val gameRepository: GameDataSource) {
    operator fun invoke(): List<Game> {
        return gameRepository.games.filter { it.isClosed }
    }
}