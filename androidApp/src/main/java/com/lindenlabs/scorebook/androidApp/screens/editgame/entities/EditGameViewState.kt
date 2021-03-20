package com.lindenlabs.scorebook.androidApp.screens.editgame.entities

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game

sealed class EditGameViewState {

    data class Initial(val game: Game) : EditGameViewState()
}
