package com.lindenlabs.scorebook.shared.common.entities.addplayers

import com.lindenlabs.scorebook.shared.common.raw.Game

sealed class AddPlayerInteraction {

    object SavePlayerDataAndExit : AddPlayerInteraction()

    data class AddAnotherPlayer(val playerName: String) : AddPlayerInteraction()

    object EmptyText : AddPlayerInteraction()

    object TextEntered : AddPlayerInteraction()

    object Typing : AddPlayerInteraction()

    object GoBackHome : AddPlayerInteraction()

}

sealed class AddPlayersViewEvent {
    data class NavigateToGameDetail(val game: Game) : AddPlayersViewEvent()
    object NavigateHome : AddPlayersViewEvent()
}

sealed class AddPlayersViewState {
    data class LoadAutocompleteAdapter(val suggestedPlayerNames: List<String>) : AddPlayersViewState()
    data class UpdateCurrentPlayersText(val playersText: String) : AddPlayersViewState()
    data class PlusButtonEnabled(val isEnabled: Boolean): AddPlayersViewState()
    object TextEntryError : AddPlayersViewState()
    object TypingState : AddPlayersViewState()
}