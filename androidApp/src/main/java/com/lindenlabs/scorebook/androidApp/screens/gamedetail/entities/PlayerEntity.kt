package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

data class PlayerEntity(val player: Player, val isPlayersTurn: Boolean = false)