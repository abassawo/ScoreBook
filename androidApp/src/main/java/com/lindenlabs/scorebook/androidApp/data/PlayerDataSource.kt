package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

interface PlayerDataSource {
    val players: List<Player>
    fun getGameById(id: Long): Player?
}