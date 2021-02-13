package com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities

internal sealed class GameInteraction() {
    data class NewGameClicked(val name: String?) : GameInteraction()
}