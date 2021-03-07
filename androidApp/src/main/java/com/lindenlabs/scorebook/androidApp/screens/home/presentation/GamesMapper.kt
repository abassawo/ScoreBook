package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GamesWrapper

class GamesMapper {

    fun mapGamesToWrapper(games: List<Game>): GamesWrapper {
        val openGames = GetOpenGames(games)()
        val closedGames = GetClosedGames(games)()
        return GamesWrapper(openGames, closedGames)
    }
}