package com.lindenlabs.scorebook.androidApp.screens.home.domain

class GetOpenGames(private val gameRepository: GameRepository = TestRepository()) {
    operator fun invoke() = gameRepository.getGames().filter { it.isInSession }

}