package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.data.GameRepository

class GetOpenGames(private val gameRepository: GameRepository) {
    operator fun invoke() = gameRepository.games.filterNot { it.isClosed }

}