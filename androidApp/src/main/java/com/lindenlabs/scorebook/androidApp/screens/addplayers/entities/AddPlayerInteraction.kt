package com.lindenlabs.scorebook.androidApp.screens.addplayers.entities

sealed class AddPlayerInteraction {

    data class NewPlayer(val name: String) : AddPlayerInteraction()

    data class AddAnotherPlayer(val previousEntries: List<String>): AddPlayerInteraction()

}