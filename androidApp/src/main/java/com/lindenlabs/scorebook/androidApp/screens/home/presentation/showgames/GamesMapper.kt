package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames
import com.lindenlabs.scorebook.shared.raw.Game

class GamesMapper {

    fun mapGamesToWrapper(games: List<Game>): GamesWrapper {
        val openGames = GetOpenGames(games)()
        val closedGames = GetClosedGames(games)()
        return GamesWrapper(openGames, closedGames)
    }
}
