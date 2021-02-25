package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository

class GetOpenGames(private val gameRepository: GameDataSource) {
    operator fun invoke() = gameRepository.games.filterNot { it.isClosed }

}