package com.lindenlabs.scorebook.androidApp.screens.editgame

<<<<<<< HEAD
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.entities.editgame.EditGameInteraction
import com.lindenlabs.scorebook.shared.common.entities.editgame.EditGameViewEvent
import com.lindenlabs.scorebook.shared.common.entities.editgame.EditGameViewState
=======
import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.raw.Game
import kotlinx.coroutines.launch
>>>>>>> Use passed in constructor values for viewmodel

class EditGameViewModel(val appRepository: AppRepository, gameId: String) : ViewModel() {
    private lateinit var game: Game
    val viewState: MutableLiveData<EditGameViewState> =
        MutableLiveData()
    val viewEvent: MutableLiveData<EditGameViewEvent> =
        MutableLiveData()

    init {
        launch(gameId)
    }

    fun launch(gameId: String) {
        viewModelScope.launch {
            game = requireNotNull(appRepository.getGame(gameId))
            viewState.value = EditGameViewState.Initial(game)
        }
    }

    fun handleInteraction(interaction: EditGameInteraction) {
        if (interaction is EditGameInteraction.Cancel) {
            viewEvent.value = EditGameViewEvent.ReturnToGameDetail(game)
        } else if (interaction is EditGameInteraction.SaveChanges) {
            val enteredText = interaction.newlyEnteredGameName
            if (enteredText.isNullOrEmpty()) {
                viewEvent.value = EditGameViewEvent.ShowTextEntryError
            } else {
                game.name = enteredText
                game.strategy = interaction.newGameStrategy
                viewModelScope.launch {
                    appRepository.updateGame(game)
                }
                viewEvent.value = EditGameViewEvent.ReturnToGameDetail(game)
            }
        }
    }
}