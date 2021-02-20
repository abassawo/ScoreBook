package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

data class PlayerEntity(
    val player: Player,
    val clickAction: (interaction: PlayerInteraction) -> Unit,
    val isPlayersTurn: Boolean = player.isPlayerTurn
)