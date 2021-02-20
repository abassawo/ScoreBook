package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class GameViewEntityMapper {

    fun map(players: List<Player>, currentPlayer: Player, interaction: (PlayerInteraction)-> Unit) : List<PlayerEntity>{
        return players.map{  player ->
            player.isPlayerTurn = player == currentPlayer
            PlayerEntity(player, { interaction(it) })
        }
    }
}
