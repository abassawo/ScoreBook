package com.lindenlabs.scorebook.androidApp.screens.victory

import com.lindenlabs.scorebook.androidApp.base.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class VictoryViewModelTest : BaseViewModelTest() {
    private val underTest = VictoryViewModel()

//    private fun initArgs(): VictoryFragmentArgs {
//        val game = gameWithPlayers()
//        game.players.first().scoreTotal = 100
//        game.players.last().scoreTotal = 20
//        return VictoryFragmentArgs((game))
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test single winner is announced`() = runBlockingTest {
        MainScope().launch {
            val emittedState = requireNotNull(underTest.viewState.value)
            assertEquals("Player 1 is the winner", emittedState.victoryText)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test stalemate is announced`() = runBlockingTest {
        MainScope().launch {
            val emittedState = requireNotNull(underTest.viewState.value)
            assertEquals("Stalemate!", emittedState.victoryText)
        }
    }
}