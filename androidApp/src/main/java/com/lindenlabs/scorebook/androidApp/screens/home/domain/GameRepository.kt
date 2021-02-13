package com.lindenlabs.scorebook.androidApp.screens.home.domain

import com.lindenlabs.scorebook.androidApp.screens.home.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

open class GameRepository : GameDataSource {

    override fun getGames(): List<Game> = listOf()

    override fun getGameById(id: UUID) : Game? = getGames().find { it.id == id }

}

