package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class PlayerRepository : PlayerDataSource {
    override val players: List<Player>
        get() = TODO("Not yet implemented")

    override fun getGameById(id: Long): Player? {
        TODO("Not yet implemented")
    }
}