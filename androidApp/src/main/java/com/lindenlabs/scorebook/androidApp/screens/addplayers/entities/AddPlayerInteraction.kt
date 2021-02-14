package com.lindenlabs.scorebook.androidApp.screens.addplayers.entities

sealed class AddPlayerInteraction(open val name: String) {

    data class NewPlayer(override val name: String) : AddPlayerInteraction(name)

    data class AddAnotherPlayer(override val name: String): AddPlayerInteraction(name)

}