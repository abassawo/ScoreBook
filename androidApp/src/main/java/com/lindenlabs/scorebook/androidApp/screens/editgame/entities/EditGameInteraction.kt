package com.lindenlabs.scorebook.androidApp.screens.editgame.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.GameStrategy

sealed class EditGameInteraction {
    data class EditGameName(val name: String) : EditGameInteraction()
    data class SetGameStrategy(val gameStrategy: GameStrategy) : EditGameInteraction()
    data class SaveChanges(val game: Game): EditGameInteraction()
    object Cancel : EditGameInteraction()
    object ClearGameName : EditGameInteraction()
}