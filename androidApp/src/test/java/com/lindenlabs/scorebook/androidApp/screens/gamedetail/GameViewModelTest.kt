package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.utils.game
import com.lindenlabs.scorebook.androidApp.utils.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameViewModel
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.mock
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest : BaseViewModelTest() {
    private val argsMock: GameDetailFragmentArgs = mock()
    private val arrangeBuilder: GameViewModelTest.ArrangeBuilder = ArrangeBuilder()
    private var underTest: GameViewModel = GameViewModel(argsMock)

    @Test
    fun `test game just created as noted by empty players list`() = runBlockingTest {
        MainScope().launch {
            val emittedEvent = underTest.viewEvent.value
            assert(emittedEvent is GameDetailEvent.AddPlayersClicked)
        }
    }

    @Test
    fun `test user click action on a player`() = runBlockingTest {
        MainScope().launch {
            arrangeBuilder.withGame(gameWithPlayers())
            underTest = GameViewModel(GameDetailFragmentArgs(gameWithPlayers()))
            underTest.handleInteraction(GameDetailInteraction.PlayerClicked(gameWithPlayers().players.first()))

        }
    }

    @Test
    fun `test on back interaction`() = runBlockingTest {
        MainScope().launch {
            underTest.handleInteraction(GameDetailInteraction.GoBack)
            val emittedEvent = underTest.viewEvent.value
            assert(emittedEvent is GameDetailEvent.GoBackHome)
        }
    }

    @Test
    fun `test end game interaction`() = runBlockingTest {
        MainScope().launch {
            underTest.handleInteraction(GameDetailInteraction.EndGameClicked)
            val emittedEvent = underTest.viewEvent.value
            assert(emittedEvent is GameDetailEvent.EndGame)
        }
    }

    inner class ArrangeBuilder {

        init {
            withGame(game())
        }

        fun withGame(game: Game) = also {
            whenever(argsMock.gameArg).thenReturn(game)
        }
    }
}