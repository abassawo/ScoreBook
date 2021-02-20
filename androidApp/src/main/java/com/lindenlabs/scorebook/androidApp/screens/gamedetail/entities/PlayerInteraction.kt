package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

sealed class PlayerInteraction {
    data class PlayerClicked(val player : Player) : PlayerInteraction()
}