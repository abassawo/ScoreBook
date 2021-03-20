package com.lindenlabs.scorebook.androidApp.screens.editgame.entities

sealed class EditGameViewEvent  {
    object ShowTextEntryError : EditGameViewEvent()
    object Exit : EditGameViewEvent()
}