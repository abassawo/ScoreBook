package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import com.lindenlabs.scorebook.androidApp.utils.gameWithPlayers
import com.lindenlabs.scorebook.androidApp.base.BaseViewModelTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdatePointsViewModelTest : BaseViewModelTest() {
    private val underTest = UpdatePointsViewModel(appRepository, initArgs())

    private fun initArgs() = UpdatePointsFragmentArgs(
            gameWithPlayers(),
            gameWithPlayers().players.first()
        )

    @Test
    fun `test adding points for first player`() =
        runBlockingTest {
            MainScope().launch {
                underTest.handleInteraction(UpdatePointsViewModel.AddPointsInteraction.AddScore(20))
                val emittedEvent = underTest.viewEvent.value
                assert(emittedEvent is UpdatePointsViewEvent.ScoreUpdated)
                verify(appRepository.dataSource).updateGame(any())
            }
        }
}