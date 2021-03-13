package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeViewState
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseViewModelTest() {
    private var underTest = HomeViewModel(appRepository)
    private val arrangeBuilder = ArrangeBuilder()

    @Test
    fun `Content with 1 open yields list of 1 open games header and 1 content body`() =
        runBlockingTest {
            MainScope().launch {
                val openGames = listOf(Game(name = "test1"))
                arrangeBuilder.withGamesLoaded(openGames)
                val emittedState = requireNotNull( underTest.viewState.value)
                assert(emittedState.entities.size == 1 + openGames.size)

                with(emittedState.entities) {
                    assertEquals(2, this.size)
                    val first = first() as GameRowEntity.HeaderType
                    assertEquals(first.title, "Open Games:")
                    val last = last() as GameRowEntity.BodyType
                    assertEquals(last.game, openGames.first())
                }
            }
        }


    @Test
    fun `Content with 1 open game and 1 closed game yields 2 headers and 2 body items`() {
        val game = Game(name = "test1")
        val game2 = Game(name = "test2", isClosed = true)
        val games = listOf(game, game2)
        runBlockingTest {
            MainScope().launch {
                arrangeBuilder.withGamesLoaded(listOf(game, game2))
                val emittedState = requireNotNull(underTest.viewState.value)
                assert(emittedState.entities.size == 2 + games.size)
            }
        }

    }

    @Test
    fun `creating a new game triggers add player screens`() {
        val interaction = GameInteraction.GameDetailsEntered("Game1")
        underTest.handleInteraction(interaction)
        val emittedEvent = requireNotNull(underTest.viewEvent.value)
        assert(emittedEvent is HomeViewEvent.ShowAddPlayersScreen)
    }

    @Test
    fun `entering empty game name should trigger error`() {
        val interaction = GameInteraction.GameDetailsEntered("")
        underTest.handleInteraction(interaction)
        val emittedEvent = requireNotNull(underTest.viewEvent.value)
        assert(emittedEvent is HomeViewEvent.AlertNoTextEntered)
    }

    @Test
    fun `clicking on a game triggers ShowActiveGame state`() {
        val game = Game(name = "test")
        val interaction = GameInteraction.GameClicked(game)
        underTest.handleInteraction(interaction)
        val emittedEvent = requireNotNull(underTest.viewEvent.value)
        with(emittedEvent as HomeViewEvent.ShowActiveGame) {
            assertEquals(this.game, game)
        }
    }

    @Test
    fun `swipe to delete interaction should trigger repository to delete game`() {
        runBlockingTest {
            MainScope().launch {
                val openGames = listOf(Game(name = "test1"))
                arrangeBuilder.withGamesLoaded(openGames)
                val game = openGames.first()
                underTest.handleInteraction(GameInteraction.SwipeToDelete(game))
                verify(mockRepo).deleteGame(game)
            }
        }
    }
}

