package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.lindenlabs.scorebook.androidApp.base.data.persistence.GameStore
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.domain.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.di.AppRepository
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever


@Rule
var rule: TestRule = InstantTaskExecutorRule()
class HomeViewModelTest {
    private val classToTest: HomeViewModel = HomeViewModel()
    private val repo: GameStore = mock(GameStore::class.java)
    private val mockRepo = PersistentGameRepository(repo)
    private val appRepository = AppRepository(mockRepo)
    private val arrangeBuilder: ArrangeBuilder = ArrangeBuilder()

    @Before
    fun test1() {
        classToTest.launch(appRepository)
    }

    @Test
    fun fetchGames() {
        val games = listOf(Game(name = "test1"))
        arrangeBuilder.withGamesLoaded(games)
        classToTest.launch(appRepository)
        val emittedState = classToTest.viewState.value
        assertTrue(emittedState!!.entities == games)
    }

    inner class ArrangeBuilder {

        fun withGamesLoaded(games: List<Game>)  {
            whenever(repo.loadAll()).thenReturn(games)
        }
    }
}