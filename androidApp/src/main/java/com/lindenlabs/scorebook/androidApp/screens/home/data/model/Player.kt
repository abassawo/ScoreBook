package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import java.util.*

data class Player(
    val name: String,
    val scoreTotal: Int = 0,
    val rounds: List<Round> = emptyList(),
    var isPlayerTurn: Boolean = false,
    val id: UUID = UUID.randomUUID()
)