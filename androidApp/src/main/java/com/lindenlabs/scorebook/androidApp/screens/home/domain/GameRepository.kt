package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.screens.home.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

open class GameRepository : GameDataSource {
    override val games : MutableList<Game> = mutableListOf()

    override fun getGameById(id: UUID) : Game? = games.find { it.id == id }

    override fun storeGame(game : Game) {
        games += game
    }
}
