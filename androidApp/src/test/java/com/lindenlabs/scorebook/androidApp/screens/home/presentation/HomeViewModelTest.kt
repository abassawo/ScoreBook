package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    private var lifeCycleTestOwner: LifeCycleTestOwner? = null

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val mockRepo: GameRepository = Mockito.mock(GameRepository::class.java)
    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope()
    val environment: Environment = Environment(mockRepo, testDispatcher, testCoroutineScope)
    private var underTest = HomeViewModel(environment)
    private val arrangeBuilder = ArrangeBuilder()


    @Before
    open fun before() {
        Dispatchers.setMain(testDispatcher)
        lifeCycleTestOwner = LifeCycleTestOwner()
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner?.onDestroy()
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun `Content with 1 open yields list of 1 open games header and 1 content body`() {
        val game = Game(name = "test1")
        val openGames = listOf(game)

        testDispatcher.runBlockingTest {
            MainScope().launch() {
                arrangeBuilder.withGamesLoaded(openGames)

                underTest.launch()

                val emittedState = underTest.viewState.getOrAwaitValue()
                assert(emittedState.entities.size == 1 + openGames.size)

                with(emittedState.entities.first() as GameRowEntity.HeaderType) {
                    assertEquals(this.title, "Open Games:")
                }

                with(emittedState.entities[1] as GameRowEntity.BodyType) {
                    assertEquals(this.game, game)
                }
            }
        }
    }

    @Test
    fun `Content with 1 open game and 1 closed game yields 2 headers and 2 body items`() {
        val game = Game(name = "test1")
        val game2 = Game(name = "test2", isClosed = true)
        val games = listOf(game, game2)
        testDispatcher.runBlockingTest {
            MainScope().launch {
                arrangeBuilder.withGamesLoaded(listOf(game, game2))
//                underTest.launch()
                val emittedState = underTest.viewState.getOrAwaitValue()
                assert(emittedState.entities.size == 2 + games.size)
            }
        }

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

    inner class ArrangeBuilder {

        suspend fun withGamesLoaded(games: List<Game>) = also {
            whenever(environment.load()).thenReturn(games)
        }
    }
}

