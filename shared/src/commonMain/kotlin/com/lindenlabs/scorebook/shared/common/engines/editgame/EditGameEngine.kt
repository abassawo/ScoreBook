package com.lindenlabs.scorebook.shared.common.engines.editgame

import com.lindenlabs.scorebook.shared.common.Environment
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.raw.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditGameEngine(val coroutineScope: CoroutineScope, val appRepository: AppRepository) {
    private lateinit var game: Game
    val viewState: MutableStateFlow<EditGameViewState> = MutableStateFlow(EditGameViewState.Loading)
    val viewEvent: MutableStateFlow<EditGameViewEvent> = MutableStateFlow(EditGameViewEvent.None)


    fun launch(gameId: String) {
        coroutineScope.launch {
            game = appRepository.getGame(gameId)
            viewState.value = EditGameViewState.Initial(game)
        }
    }

    fun handleInteraction(interaction: EditGameInteraction) {
        if (interaction is EditGameInteraction.Cancel) {
            viewEvent.value = EditGameViewEvent.ReturnToGameDetail(game)
        }
        else if (interaction is EditGameInteraction.SaveChanges) {
            val enteredText = interaction.newlyEnteredGameName
            if (enteredText.isNullOrEmpty()) {
                viewEvent.value = EditGameViewEvent.ShowTextEntryError
            } else {
                game.name = enteredText
                game.strategy = interaction.newGameStrategy
                coroutineScope.launch {
                    appRepository.updateGame(game)
                }
                viewEvent.value = EditGameViewEvent.ReturnToGameDetail(game)
            }
        }
    }
}