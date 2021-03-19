package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

sealed class GameDetailViewState(open val gameName: String) {

    data class NotStarted(override val gameName: String) : GameDetailViewState(gameName)


    data class StartedWithPlayers(val playerData: List<PlayerDataEntity>, override val gameName: String) :
        GameDetailViewState(gameName)


    data class ClosedGame(val playerData: List<PlayerDataEntity>, override val gameName: String) :
        GameDetailViewState(gameName)

}