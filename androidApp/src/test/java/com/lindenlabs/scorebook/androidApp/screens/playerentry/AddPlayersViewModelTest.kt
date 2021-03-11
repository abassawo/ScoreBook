package com.lindenlabs.scorebook.androidApp.screens.playerentry

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.BaseTest
import com.lindenlabs.scorebook.androidApp.screens.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersViewState.*
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddPlayersViewModelTest : BaseTest() {
    private val testCoroutineScope = TestCoroutineScope()
    private val underTest = AddPlayersViewModel(
        environment,
        AddPlayersFragmentArgs(gameWithPlayers()),
        TestCoroutineScope()
    )

    @Test
    fun `autocomplete player names state is triggered when there are existing games with players`() {
        val game = Game(name = "test1")
        val openGames = listOf(game)
        testCoroutineScope.runBlockingTest {
            arrangeBuilder.withGamesLoaded(openGames)

        }

        underTest.launch()
        assert(underTest.viewState.getOrAwaitValue() is LoadAutocompleteAdapter)
    }

    @Test
    fun `game with players already added triggers UpdateCurrentPlayersText`() {
        runBlocking { arrangeBuilder.withGamesLoaded(listOf(gameWithPlayers())) }
    }

    @Test
    fun `on updating player name text field, validation should occur`() {
        underTest.launch()
        underTest.handleInteraction(TextEntered)
        assert(underTest.viewState.getOrAwaitValue() is PlusButtonEnabled)
    }

    @Test
    fun `valid first player name yields update player text`() {
        underTest.launch()
        underTest.handleInteraction(AddAnotherPlayer("Player 1"))
        val emittedState = underTest.viewState.getOrAwaitValue() as UpdateCurrentPlayersText
        assertEquals("Player 1", emittedState.playersText)
    }


    @Test
    fun `valid multiple player names yield update player text`() =
        underTest.run {
            launch()
            handleInteraction(AddAnotherPlayer("Player 1"))
            handleInteraction(AddAnotherPlayer("Player 2"))
            handleInteraction(AddAnotherPlayer("Player 3"))
            val emittedState =
                viewState.getOrAwaitValue() as UpdateCurrentPlayersText
            assertEquals("Player 1, Player 2, Player 3", emittedState.playersText)
        }

    @Test
    fun `Saving valid players data allows progression to score screen`() {
        underTest.run {
            launch()
            handleInteraction(AddAnotherPlayer("Player 1"))
            handleInteraction(AddAnotherPlayer("Player 2"))
            handleInteraction(SavePlayerDataAndExit("Player 3"))
            assert(viewEvent.getOrAwaitValue() is AddPlayersViewEvent.NavigateToGameDetail)
        }
    }

    @Test
    fun `test navigating back home`() =
        underTest.run {
            launch()
            underTest.handleInteraction(GoBackHome)
            assert(underTest.viewEvent.getOrAwaitValue() is AddPlayersViewEvent.NavigateHome)
        }

    @Test
    fun `assert text entry error is emitted when entering empty text`() {
        underTest.run {
            launch()
            underTest.handleInteraction(AddAnotherPlayer(""))
            assert(underTest.viewState.getOrAwaitValue() is TextEntryError)
        }
    }

    @Test
    fun `plus button disabled when empty text`() =
        underTest.run {
            launch()
            underTest.handleInteraction(EmptyText)
            assert(underTest.viewState.getOrAwaitValue() is PlusButtonEnabled)
        }

}