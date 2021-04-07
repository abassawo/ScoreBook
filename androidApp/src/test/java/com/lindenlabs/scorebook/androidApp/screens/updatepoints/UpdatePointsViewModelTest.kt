package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import com.lindenlabs.scorebook.androidApp.base.BaseViewModelTest
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdatePointsViewModelTest : BaseViewModelTest() {
    private val underTest = UpdatePointsViewModel()

    @Test
    fun `test adding points for first player`() =
        runBlockingTest {
            MainScope().launch {
//                underTest.handleInteraction(UpdatePointsViewModel.UpdatePointsInteraction.AddScore(20))
//                val emittedEvent = underTest.viewEvent.value
//                assert(emittedEvent is UpdatePointsViewEvent.ScoreUpdated)
//                verify(appRepository.gamesDataSource).updateGame(any())
            }
        }
}