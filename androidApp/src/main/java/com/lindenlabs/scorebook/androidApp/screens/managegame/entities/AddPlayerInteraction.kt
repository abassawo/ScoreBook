package com.lindenlabs.scorebook.androidApp.screens.managegame.entities

sealed class AddPlayerInteraction {

    data class SavePlayerDataAndExit(val playerName: String) : AddPlayerInteraction()

    data class AddAnotherPlayer(val playerName: String) : AddPlayerInteraction()

    object EmptyText : AddPlayerInteraction()

    object TextEntered : AddPlayerInteraction()

    object Typing : AddPlayerInteraction()

}