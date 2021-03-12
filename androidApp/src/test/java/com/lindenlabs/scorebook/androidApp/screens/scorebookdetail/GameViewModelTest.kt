package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.game
import com.lindenlabs.scorebook.androidApp.screens.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
            val emittedEvent = underTest.viewEvent.getOrAwaitValue()
            assert(emittedEvent is ScoreBookViewEvent.AddPlayersClicked)
        }
    }

    @Test
    fun `test user click action on a player`() = runBlockingTest {
        MainScope().launch {
            arrangeBuilder.withGame(gameWithPlayers())
            underTest = GameViewModel(GameDetailFragmentArgs(gameWithPlayers()))
            underTest.handleInteraction(ScoreBookInteraction.PlayerClicked(gameWithPlayers().players.first()))

        }
    }

    @Test
    fun `test on back interaction`() = runBlockingTest {
        MainScope().launch {
            underTest.handleInteraction(ScoreBookInteraction.GoBack)
            val emittedEvent = underTest.viewEvent.getOrAwaitValue()
            assert(emittedEvent is ScoreBookViewEvent.GoBackHome)
        }
    }

    @Test
    fun `test end game interaction`() = runBlockingTest {
        MainScope().launch {
            underTest.handleInteraction(ScoreBookInteraction.EndGameClicked)
            val emittedEvent = underTest.viewEvent.getOrAwaitValue()
            assert(emittedEvent is ScoreBookViewEvent.EndGame)
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