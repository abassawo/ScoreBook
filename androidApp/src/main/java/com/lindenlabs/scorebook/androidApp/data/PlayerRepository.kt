package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class PlayerRepository : PlayerDataSource {
    override val players: MutableList<Player> = mutableListOf()

    override fun addPlayer(player: Player) {
        players += player
    }
}