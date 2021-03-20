package com.lindenlabs.scorebook.androidApp.screens.editgame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameInteraction
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditGameViewModel @Inject constructor(
    val appRepository: AppRepository,
    val args: EditGameFragmentArgs
) : ViewModel() {
    private val viewState: MutableLiveData<EditGameViewState> = makeInitialState()
    private val viewEvent: MutableLiveData<EditGameViewEvent> = MutableLiveData<EditGameViewEvent>()

    private fun makeInitialState(): MutableLiveData<EditGameViewState> =
        MutableLiveData(EditGameViewState.Initial(args.gameArg))

    fun handleInteraction(interaction: EditGameInteraction) {
        if (interaction is EditGameInteraction.Cancel)
            viewEvent.postValue(EditGameViewEvent.Exit)
        else if (interaction is EditGameInteraction.SaveChanges) {
            val enteredText = interaction.newlyEnteredGameName
            if (enteredText.isNullOrEmpty()) {
                viewEvent.postValue(EditGameViewEvent.ShowTextEntryError)
            } else {
                args.gameArg.name = enteredText
                args.gameArg.strategy = interaction.newGameStrategy
                viewModelScope.launch {
                    appRepository.updateGame(args.gameArg)
                }
                viewEvent.postValue(EditGameViewEvent.Exit)
            }
        }
    }
}