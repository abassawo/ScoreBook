package com.lindenlabs.scorebook.androidApp.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.LifeCycleTestOwner
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito

open class BaseTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner
    private val mockRepo: GameRepository = Mockito.mock(GameRepository::class.java)
    val environment: Environment = Environment(mockRepo)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    val arrangeBuilder = ArrangeBuilder()

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

    inner class ArrangeBuilder {

        suspend fun withGamesLoaded(games: List<Game>) = also {
            whenever(environment.load()).thenReturn(games)
        }
    }
}