package com.lindenlabs.scorebook.shared.common.entities.editgame

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy

sealed class EditGameViewState {
    data class Initial(val game: Game) : EditGameViewState()
}

sealed class EditGameViewEvent  {
    object ShowTextEntryError : EditGameViewEvent()
    data class ReturnToGameDetail(val game: Game) : EditGameViewEvent()
}

sealed class EditGameInteraction {
    object Cancel : EditGameInteraction()

    data class SaveChanges(
        val newlyEnteredGameName: String,
        val newGameStrategy: GameStrategy
    ) : EditGameInteraction()
}