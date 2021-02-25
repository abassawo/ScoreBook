package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities

import com.lindenlabs.scorebook.androidApp.data.model.Player

data class ScoreBookEntity(
    val player: Player,
    val clickAction: (interaction: ScoreBookInteraction) -> Unit,
    val isPlayersTurn: Boolean = player.isPlayerTurn
)