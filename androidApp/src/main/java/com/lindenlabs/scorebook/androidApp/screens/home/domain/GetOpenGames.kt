package com.lindenlabs.scorebook.androidApp.screens.home.domain

class GetOpenGames(private val gameRepository: GameRepository) {
    operator fun invoke() = gameRepository.games.filterNot { it.isClosed }

}