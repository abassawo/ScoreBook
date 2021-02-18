package com.lindenlabs.scorebook.androidApp.screens.managegame.entities

sealed class AddPlayerInteraction(open val playerName: String) {

    data class SavePlayerDataAndExit(override val playerName: String) : AddPlayerInteraction(playerName)

    data class AddAnotherPlayer(override val playerName: String): AddPlayerInteraction(playerName)

}