package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner
    private val classToTest: HomeViewModel = HomeViewModel()
    private val mockRepo: GameRepository = mock(GameRepository::class.java)
    private val environment: Environment = Environment(mockRepo)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val arrangeBuilder: ArrangeBuilder = ArrangeBuilder()

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        lifeCycleTestOwner = LifeCycleTestOwner()
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner.onDestroy()
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `Content with 1 open yields list of 1 open games header and 1 content body`() {
        val game = Game(name = "test1")
        val openGames = listOf(game)
        runBlocking { arrangeBuilder.withGamesLoaded(openGames) }

        classToTest.launch(environment)
        val emittedState = classToTest.viewState.getOrAwaitValue()
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
        classToTest.launch(environment)
        val emittedState = classToTest.viewState.getOrAwaitValue()
        assert(emittedState.entities.size == 2 + games.size)
    }

    @Test
    fun `creating a new game triggers add player screens`() {
        val interaction = GameInteraction.GameDetailsEntered("Game1")
        classToTest.handleInteraction(interaction)
        val emittedEvent = classToTest.viewEvent.getOrAwaitValue()
        assert(emittedEvent is HomeViewEvent.ShowAddPlayersScreen)
    }

    @Test
    fun `entering empty game name should trigger error`() {
        val interaction = GameInteraction.GameDetailsEntered("")
        classToTest.handleInteraction(interaction)
        val emittedEvent = classToTest.viewEvent.getOrAwaitValue()
        assert(emittedEvent is HomeViewEvent.AlertNoTextEntered)
    }

    @Test
    fun `clicking on a game triggers ShowActiveGame state`() {
        val game = Game(name = "test")
        val interaction = GameInteraction.GameClicked(game)
        classToTest.handleInteraction(interaction)
        val emittedEvent = classToTest.viewEvent.getOrAwaitValue()
        with(emittedEvent as HomeViewEvent.ShowActiveGame) {
            assertEquals(this.game, game)
        }

    }

    inner class ArrangeBuilder {

        suspend fun withGamesLoaded(games: List<Game>) = also {
            whenever(environment.load()).thenReturn(games)
        }
    }
}
