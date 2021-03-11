package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import com.lindenlabs.scorebook.androidApp.screens.BaseTest
import com.lindenlabs.scorebook.androidApp.screens.gameWithPlayers
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UpdatePointsViewModelTest : BaseTest() {
    private val underTest = UpdatePointsViewModel(environment, initArgs())

    private fun initArgs() = UpdatePointsFragmentArgs(
            gameWithPlayers(),
            gameWithPlayers().players.first()
        )

    @Test
    fun `test adding points for first player`() =
        runBlocking {
            underTest.handleInteraction(UpdatePointsViewModel.AddPointsInteraction.AddScore(20))
            val emittedEvent = underTest.viewEvent.value
            assert(emittedEvent is UpdatePointsViewEvent.ScoreUpdated)
            verify(environment.gamesRepo).updateGame(any())
        }
}