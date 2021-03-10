package com.lindenlabs.scorebook.androidApp.screens.playerentry

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.domain.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.LifeCycleTestOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito


@ExperimentalCoroutinesApi
class AddPlayersViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner
    private val classToTest: AddPlayersViewModel = AddPlayersViewModel()
    private val mockRepo: GameRepository = Mockito.mock(GameRepository::class.java)
    private val environment: Environment = Environment(mockRepo)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

}
