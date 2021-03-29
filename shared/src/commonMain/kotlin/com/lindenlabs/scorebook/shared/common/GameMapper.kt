package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Game

class GamesMapper {

    fun mapGamesToWrapper(games: List<Game>): GamesWrapper {
        val openGames = GetOpenGames(games)()
        val closedGames = GetClosedGames(games)()
        return GamesWrapper(openGames, closedGames)
    }
}

data class GamesWrapper(val openGames: List<Game>, val closedGames: List<Game>)

class GetOpenGames(private val games: List<Game>) {
    operator fun invoke() = games.filterNot { it.isClosed }
}

class GetClosedGames(private val games: List<Game>) {
    operator fun invoke(): List<Game> {
        return games.filter { it.isClosed }
    }
}


