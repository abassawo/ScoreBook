package com.lindenlabs.scorebook.androidApp.screens.home.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

interface GameDataSource {
    fun getGames() : List<Game>
    fun getGameById(id: UUID) : Game?
}