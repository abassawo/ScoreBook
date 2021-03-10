package com.lindenlabs.scorebook.androidApp.screens.playerentry

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.BaseTest
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.getOrAwaitValue
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test


@ExperimentalCoroutinesApi
class AddPlayersViewModelTest : BaseTest(){

    private val classToTest: AddPlayersViewModel = AddPlayersViewModel()

    @Test
    fun `autocomplete player names state is triggered when there are existing games with players`() {
        val game = Game(name="test")
        runBlocking {
            arrangeBuilder.withGamesLoaded(listOf(game))
        }
        val args = AddPlayersFragmentArgs(game)
        classToTest.launch(environment, args)
        val emittedState = classToTest.viewState.getOrAwaitValue()
        assert(emittedState is AddPlayersViewState.LoadAutocompleteAdapter)
    }

    @Test
    fun `on entering text, validation should occur`() {
        val game = Game(name="test")
        val args = AddPlayersFragmentArgs(game)
        classToTest.launch(environment, args)

        classToTest.handleInteraction(AddPlayerInteraction.TextEntered)
        val emittedState = classToTest.viewState.getOrAwaitValue()
        assert(emittedState is AddPlayersViewState.ValidateTextForPlusButton)
    }
}
