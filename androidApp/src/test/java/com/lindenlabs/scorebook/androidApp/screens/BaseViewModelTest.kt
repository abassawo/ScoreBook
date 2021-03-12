package com.lindenlabs.scorebook.androidApp.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lindenlabs.scorebook.androidApp.CoroutineTestRule
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.LifeCycleTestOwner
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito

open class BaseViewModelTest  {
    var lifeCycleTestOwner: LifeCycleTestOwner? = null

    val mockRepo: GameRepository = Mockito.mock(GameRepository::class.java)

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    val environment: Environment = Environment(mockRepo)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    open fun before() {
        lifeCycleTestOwner = LifeCycleTestOwner()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        lifeCycleTestOwner?.onDestroy()
    }

    inner class ArrangeBuilder {

        suspend fun withGamesLoaded(games: List<Game>) = also {
            whenever(environment.load()).thenReturn(games)
        }
    }
}