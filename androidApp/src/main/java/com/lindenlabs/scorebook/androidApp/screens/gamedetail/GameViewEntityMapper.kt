package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class GameViewEntityMapper {

    fun map(players: List<Player>, interaction: (PlayerInteraction)-> Unit) : List<PlayerEntity>{
        return players.map{  player ->
            PlayerEntity(player, { interaction(it) })
        }
    }
}
