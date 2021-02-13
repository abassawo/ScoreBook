package com.lindenlabs.scorebook.androidApp.screens.home.domain

class GetClosedGames(private val gameRepository: GameRepository) {
    operator fun invoke() = gameRepository.getGames().filterNot { it.isInSession }
}