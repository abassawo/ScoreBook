package com.lindenlabs.scorebook.androidApp.screens

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player


fun game() = Game(name = "Test")

fun gameWithPlayers() =
    game().copy(players = listOf(Player(name = "Player1"), Player(name = "Player2")))