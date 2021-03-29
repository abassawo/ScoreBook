package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers

import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerDataEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.shared.raw.Player

class GameViewEntityMapper {

    fun map(players: List<Player>, interaction: (GameDetailInteraction)-> Unit) : List<PlayerDataEntity>{
        return players.map{  player ->
            PlayerDataEntity(player, { interaction(it) })
        }
    }
}
