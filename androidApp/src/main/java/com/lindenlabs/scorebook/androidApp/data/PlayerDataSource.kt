package com.lindenlabs.scorebook.androidApp.data

import com.lindenlabs.scorebook.androidApp.data.model.Player


interface PlayerDataSource {
    val players: List<Player>
    fun getGameById(id: Long): Player?
}