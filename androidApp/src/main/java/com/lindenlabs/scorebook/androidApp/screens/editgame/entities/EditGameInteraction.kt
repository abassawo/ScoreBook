package com.lindenlabs.scorebook.androidApp.screens.editgame.entities

import com.lindenlabs.scorebook.shared.raw.GameStrategy


sealed class EditGameInteraction {
    object Cancel : EditGameInteraction()

    data class SaveChanges(
        val newlyEnteredGameName: String,
        val newGameStrategy: GameStrategy
    ) : EditGameInteraction()
}