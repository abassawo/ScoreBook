package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

data class PlayerDataEntity(
    val player: Player,
    val clickAction: (interaction: GameDetailInteraction) -> Unit,
    val isPlayersTurn: Boolean = player.isPlayerTurn
)