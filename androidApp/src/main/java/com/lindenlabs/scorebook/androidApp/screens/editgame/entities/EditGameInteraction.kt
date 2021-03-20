package com.lindenlabs.scorebook.androidApp.screens.editgame.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.GameStrategy

sealed class EditGameInteraction {
    object Cancel : EditGameInteraction()

    data class SaveChanges(
        val newlyEnteredGameName: String,
        val newGameStrategy: GameStrategy
    ) : EditGameInteraction()
}