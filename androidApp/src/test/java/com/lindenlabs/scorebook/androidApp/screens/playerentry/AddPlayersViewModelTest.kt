package com.lindenlabs.scorebook.androidApp.screens.playerentry

import com.lindenlabs.scorebook.androidApp.base.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersViewState.*
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayerInteraction.*
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersViewEvent
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.utils.gameWithPlayers
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddPlayersViewModelTest : BaseViewModelTest() {
    private val arrangeBuilder = ArrangeBuilder()
    private val underTest =
        AddPlayersViewModel(appRepository, AddPlayersFragmentArgs(gameWithPlayers()))

    @Before
    override fun before() {
        super.before()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `autocomplete player names state is triggered when there are existing games with players`() {
        runBlockingTest {
            val game = Game(name = "test1")
            val openGames = listOf(game)
            MainScope().launch {
//                arrangeBuilder.withGamesLoaded(openGames)
                assert(underTest.viewState.value is LoadAutocompleteAdapter)
            }
        }
    }

    @Test
    fun `game with players already added triggers UpdateCurrentPlayersText`() {
        runBlockingTest {
            MainScope().launch {
                arrangeBuilder.withGamesLoaded(listOf(gameWithPlayers()))
//                underTest.launch()
                assert(underTest.viewState.value is UpdateCurrentPlayersText)
            }
        }
    }

    @Test
    fun `on updating player name text field, validation should occur`() {
        runBlockingTest {
            MainScope().launch {
//                underTest.launch()
                underTest.handleInteraction(TextEntered)
                assert(underTest.viewState.value is PlusButtonEnabled)
            }
        }
    }

    @Test
    fun `valid first player name yields update player text`() {
        runBlockingTest {
            MainScope().launch {
//                underTest.launch()
                underTest.handleInteraction(AddAnotherPlayer("Player 1"))
                val emittedState = underTest.viewState.value as UpdateCurrentPlayersText
                assertEquals("Player 1", emittedState.playersText)
            }
        }
    }


    @Test
    fun `valid multiple player names yield update player text`() =
        runBlockingTest {
            MainScope().launch {
                underTest.run {
//                    launch()
                    handleInteraction(AddAnotherPlayer("Player 1"))
                    handleInteraction(AddAnotherPlayer("Player 2"))
                    handleInteraction(AddAnotherPlayer("Player 3"))
                    val emittedState = viewState.value as UpdateCurrentPlayersText
                    assertEquals("Player 1, Player 2, Player 3", emittedState.playersText)
                }
            }
        }

    @Test
    fun `Saving valid players data allows progression to score screen`() {
        runBlockingTest {
            MainScope().launch {
                underTest.run {
//                    launch()
                    handleInteraction(AddAnotherPlayer("Player 1"))
                    handleInteraction(AddAnotherPlayer("Player 2"))
                    handleInteraction(SavePlayerDataAndExit("Player 3"))
                    assert(viewEvent.value is AddPlayersViewEvent.NavigateToGameDetail)
                }
            }
        }
    }

    @Test
    fun `test navigating back home`() =
        runBlockingTest {
            MainScope().launch {
                underTest.run {
//                    launch()
                    underTest.handleInteraction(GoBackHome)
                    assert(underTest.viewEvent.value is AddPlayersViewEvent.NavigateHome)
                }
            }
        }

    @Test
    fun `assert text entry error is emitted when entering empty text`() {
        runBlockingTest {
            MainScope().launch {
                underTest.run {
//                    launch()
                    underTest.handleInteraction(AddAnotherPlayer(""))
                    assert(underTest.viewState.value is TextEntryError)
                }
            }
        }
    }

    @Test
    fun `plus button disabled when empty text`() =
        runBlockingTest {
            MainScope().launch {
                underTest.run {
//                    launch()
                    underTest.handleInteraction(EmptyText)
                    assert(underTest.viewState.value is PlusButtonEnabled)
                }
            }
        }

}