package com.lindenlabs.scorebook.androidApp.screens.editgame.entities

import com.lindenlabs.scorebook.shared.common.raw.Game


sealed class EditGameViewEvent  {
    object ShowTextEntryError : EditGameViewEvent()
    data class ReturnToGameDetail(val game: Game) : EditGameViewEvent()
}