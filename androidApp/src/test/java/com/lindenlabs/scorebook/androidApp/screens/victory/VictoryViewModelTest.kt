package com.lindenlabs.scorebook.androidApp.screens.victory

import com.lindenlabs.scorebook.androidApp.screens.BaseTest
import com.lindenlabs.scorebook.androidApp.screens.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class VictoryViewModelTest : BaseTest() {
    private val testCoroutineScope = TestCoroutineScope()
    private val underTest = VictoryViewModel(environment, initArgs(),  TestCoroutineScope())

    fun initArgs(): VictoryFragmentArgs {
        val game = gameWithPlayers()
        game.players.first().scoreTotal = 100
        game.players.last().scoreTotal = 20
        return VictoryFragmentArgs((game))
    }

    @Test
    fun `test single winner is announced`() = testCoroutineScope.runBlockingTest {
        val emittedState = underTest.viewState.getOrAwaitValue()
        assertEquals("Player 1 is the winner", emittedState.victoryText)
    }

    @Test
    fun `test stalemate is announced`() {
        val emittedState = underTest.viewState.getOrAwaitValue()
        assertEquals("Stalemate!", emittedState.victoryText)
    }
}