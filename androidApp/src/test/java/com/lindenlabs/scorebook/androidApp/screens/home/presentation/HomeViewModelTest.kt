package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

class HomeViewModelTest : BaseViewModelTest<HomeViewModel>() {

    override fun initViewModel(): HomeViewModel = HomeViewModel()

    @Test
    fun `Content with 1 open yields list of 1 open games header and 1 content body`() {
        val game = Game(name = "test1")
        val openGames = listOf(game)
        runBlocking { arrangeBuilder.withGamesLoaded(openGames) }

        underTest.launch(environment)
        val emittedState = underTest.viewState.getOrAwaitValue()
        assert(emittedState.entities.size == 1 + openGames.size)

        with(emittedState.entities.first() as GameRowEntity.HeaderType) {
            assertEquals(this.title, "Open Games:")
        }

        with(emittedState.entities[1] as GameRowEntity.BodyType) {
            assertEquals(this.game, game)
        }
    }

    @Test
    fun `Content with 1 open game and 1 closed game yields 2 headers and 2 body items`() {
        val game = Game(name = "test1")
        val game2 = Game(name = "test2", isClosed = true)
        val games = listOf(game, game2)
        runBlocking { arrangeBuilder.withGamesLoaded(listOf(game, game2)) }
        underTest.launch(environment)
        val emittedState = underTest.viewState.getOrAwaitValue()
        assert(emittedState.entities.size == 2 + games.size)
    }

    @Test
    fun `creating a new game triggers add player screens`() {
        val interaction = GameInteraction.GameDetailsEntered("Game1")
        underTest.handleInteraction(interaction)
        val emittedEvent = underTest.viewEvent.getOrAwaitValue()
        assert(emittedEvent is HomeViewEvent.ShowAddPlayersScreen)
    }

    @Test
    fun `entering empty game name should trigger error`() {
        val interaction = GameInteraction.GameDetailsEntered("")
        underTest.handleInteraction(interaction)
        val emittedEvent = underTest.viewEvent.getOrAwaitValue()
        assert(emittedEvent is HomeViewEvent.AlertNoTextEntered)
    }

    @Test
    fun `clicking on a game triggers ShowActiveGame state`() {
        val game = Game(name = "test")
        val interaction = GameInteraction.GameClicked(game)
        underTest.handleInteraction(interaction)
        val emittedEvent = underTest.viewEvent.getOrAwaitValue()
        with(emittedEvent as HomeViewEvent.ShowActiveGame) {
            assertEquals(this.game, game)
        }
    }
}
