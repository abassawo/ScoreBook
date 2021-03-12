//package com.lindenlabs.scorebook.androidApp.screens
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.lindenlabs.scorebook.androidApp.base.Environment
//import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
//import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
//import com.lindenlabs.scorebook.androidApp.screens.home.presentation.LifeCycleTestOwner
//import com.nhaarman.mockitokotlin2.whenever
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.*
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.rules.TestRule
//import org.mockito.Mockito
//import org.mockito.MockitoAnnotations
//
//@ExperimentalCoroutinesApi
//abstract class BaseTest {
//    private val mockRepo: GameRepository = Mockito.mock(GameRepository::class.java)
//    val environment: Environment = Environment(mockRepo, testDispatcher)
//
//    @get:Rule
//    var rule: TestRule = InstantTaskExecutorRule()
//    val arrangeBuilder = ArrangeBuilder()
//
//
//    inner class ArrangeBuilder {
//
//        suspend fun withGamesLoaded(games: List<Game>) = also {
//            whenever(environment.load()).thenReturn(games)
//        }
//    }
//}