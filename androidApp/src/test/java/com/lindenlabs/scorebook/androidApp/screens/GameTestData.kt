package com.lindenlabs.scorebook.androidApp.screens

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.mockito.Mockito


fun game() = Game(name = "Test")

fun gameWithPlayers() =
    game().copy(players = listOf(Player(name = "Player1"), Player(name = "Player2")))
