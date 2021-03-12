package com.lindenlabs.scorebook.androidApp.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lindenlabs.scorebook.androidApp.utils.CoroutineTestRule
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.data.source.LocalGameDataSource
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito

open class BaseViewModelTest  {
//    var lifeCycleTestOwner: LifeCycleTestOwner? = null

    private val mockRepo: LocalGameDataSource = Mockito.mock(LocalGameDataSource::class.java)
    val appRepository: AppRepository = AppRepository(mockRepo)

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    open fun before() {
//        lifeCycleTestOwner = LifeCycleTestOwner()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
//        lifeCycleTestOwner?.onDestroy()
    }

    inner class ArrangeBuilder {

        suspend fun withGamesLoaded(games: List<Game>) = also {
            whenever(appRepository.load()).thenReturn(games)
        }
    }
}