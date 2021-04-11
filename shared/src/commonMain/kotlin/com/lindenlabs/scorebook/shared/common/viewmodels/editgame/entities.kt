package com.lindenlabs.scorebook.shared.common.viewmodels.editgame

import com.lindenlabs.scorebook.shared.common.viewmodels.BaseInteraction
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy

sealed class EditGameViewState {
    object Loading : EditGameViewState()
    data class Initial(val game: Game) : EditGameViewState()
}

sealed class EditGameViewEvent  {
    object None : EditGameViewEvent()
    object ShowTextEntryError : EditGameViewEvent()
    data class ReturnToGameDetail(val game: Game) : EditGameViewEvent()
}

sealed class EditGameInteraction : BaseInteraction {
    object Cancel : EditGameInteraction()

    data class SaveChanges(
        val newlyEnteredGameName: String,
        val newGameStrategy: GameStrategy
    ) : EditGameInteraction()
}