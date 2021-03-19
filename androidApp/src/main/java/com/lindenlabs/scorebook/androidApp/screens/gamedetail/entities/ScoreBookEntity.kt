package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Player

data class ScoreBookEntity(
    val player: Player,
    val clickAction: (interaction: ScoreBookInteraction) -> Unit,
    val isPlayersTurn: Boolean = player.isPlayerTurn
)