package com.lindenlabs.scorebook.androidApp.screens.playerentry

import com.lindenlabs.scorebook.androidApp.base.Argument
import com.lindenlabs.scorebook.androidApp.screens.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.game
import com.lindenlabs.scorebook.androidApp.screens.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersViewState.*
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddPlayersViewModelTest : BaseViewModelTest<AddPlayersViewModel>() {

    override fun args(): Argument = Argument.AddPlayers(AddPlayersFragmentArgs(game()))

    override fun initViewModel(): AddPlayersViewModel = AddPlayersViewModel()

    @Test
    fun `autocomplete player names state is triggered when there are existing games with players`() {
        runBlocking { arrangeBuilder.withGamesLoaded(listOf(game())) }
        startViewModel {
            assert(it.viewState.getOrAwaitValue() is LoadAutocompleteAdapter)
        }
    }

    @Test
    fun `game with players already added triggers UpdateCurrentPlayersText`() {
        runBlocking { arrangeBuilder.withGamesLoaded(listOf(gameWithPlayers())) }
    }

    @Test
    fun `on updating player name text field, validation should occur`() =
        startViewModel { viewModel ->
            viewModel.run {
                handleInteraction(TextEntered)
                assert(viewState.getOrAwaitValue() is PlusButtonEnabled)
            }
        }

    @Test
    fun `valid first player name yields update player text`() =
        startViewModel { viewModel ->
            viewModel.run {
                handleInteraction(AddAnotherPlayer("Player 1"))
                val emittedState = viewState.getOrAwaitValue() as UpdateCurrentPlayersText
                assertEquals("Player 1", emittedState.playersText)
            }
        }


    @Test

    fun `valid multiple player names yield update player text`() =
        startViewModel { viewModel ->
            viewModel.run {
                handleInteraction(AddAnotherPlayer("Player 1"))
                handleInteraction(AddAnotherPlayer("Player 2"))
                handleInteraction(AddAnotherPlayer("Player 3"))
                val emittedState =
                    viewState.getOrAwaitValue() as UpdateCurrentPlayersText
                assertEquals("Player 1, Player 2, Player 3", emittedState.playersText)
            }
        }

    @Test
    fun `Saving valid players data allows progression to score screen`() =
        startViewModel { viewModel ->
            viewModel.run {
                handleInteraction(AddAnotherPlayer("Player 1"))
                handleInteraction(AddAnotherPlayer("Player 2"))
                handleInteraction(SavePlayerDataAndExit("Player 3"))
                assert(viewEvent.getOrAwaitValue() is AddPlayersViewEvent.NavigateToGameDetail)
            }
        }

    @Test
    fun `test navigating back home`() =
        startViewModel { viewModel ->
            viewModel.handleInteraction(GoBackHome)
            assert(viewModel.viewEvent.getOrAwaitValue() is AddPlayersViewEvent.NavigateHome)
        }

    @Test
    fun `assert text entry error is emitted when entering empty text`() {
        startViewModel { viewModel ->
            viewModel.handleInteraction(AddAnotherPlayer(""))
            assert(viewModel.viewState.getOrAwaitValue() is TextEntryError)
        }
    }

    @Test
    fun `plus button disabled when empty text`() {
        startViewModel { viewModel ->
            viewModel.handleInteraction(EmptyText)
            assert(viewModel.viewState.getOrAwaitValue() is PlusButtonEnabled)
        }
    }
}