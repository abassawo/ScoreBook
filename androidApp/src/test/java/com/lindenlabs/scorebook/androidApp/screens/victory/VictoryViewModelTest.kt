package com.lindenlabs.scorebook.androidApp.screens.victory

import com.lindenlabs.scorebook.androidApp.screens.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.screens.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class VictoryViewModelTest : BaseViewModelTest() {
//    private val testCoroutineScope = TestCoroutineScope()
    private val underTest = VictoryViewModel(environment, initArgs())

    fun initArgs(): VictoryFragmentArgs {
        val game = gameWithPlayers()
        game.players.first().scoreTotal = 100
        game.players.last().scoreTotal = 20
        return VictoryFragmentArgs((game))
    }

    @Test
    fun `test single winner is announced`() = runBlockingTest {
        MainScope().launch {
            val emittedState = underTest.viewState.getOrAwaitValue()
            assertEquals("Player 1 is the winner", emittedState.victoryText)
        }
    }

    @Test
    fun `test stalemate is announced`() {
        val emittedState = underTest.viewState.getOrAwaitValue()
        assertEquals("Stalemate!", emittedState.victoryText)
    }
}