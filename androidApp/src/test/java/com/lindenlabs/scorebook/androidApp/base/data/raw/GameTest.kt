package com.lindenlabs.scorebook.androidApp.base.data.raw

import com.lindenlabs.scorebook.androidApp.utils.gameWithPlayers
import junit.framework.Assert.assertEquals
import org.junit.Test


class GameTest {

    @Test
    fun announceWinners() {
        val game = gameWithPlayers()
        val player1 = game.players.first()
        val player2 = game.players.last()
        player1.scoreTotal = 100
        player2.scoreTotal = 20
        val score = game.end()
        assertEquals("${player1.name} is the winner!", score)
    }

    @Test
    fun announceStalemate() {
        val game = gameWithPlayers()
        val player1 = game.players.first()
        val player2 = game.players.last()
        player1.scoreTotal = 100
        player2.scoreTotal = 100
        val score = game.end()
        assertEquals("Stalemate!", score)
    }
}