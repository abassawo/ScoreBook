package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import com.lindenlabs.scorebook.androidApp.screens.game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import org.junit.Test

class GameViewModelTest {
    private val underTest: GameViewModel = GameViewModel(GameDetailFragmentArgs(game()))

    @Test
    fun `test game just created as noted by empty players list` () {
        val emittedEvent = underTest.viewEvent.getOrAwaitValue()
        assert(emittedEvent is ScoreBookViewEvent.AddPlayersClicked)
    }
}