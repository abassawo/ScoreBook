package com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities

import com.lindenlabs.scorebook.shared.raw.Game


sealed class GameDetailViewState(open val game: Game) {

    data class NotStarted(override val game: Game) : GameDetailViewState(game)


    data class StartedWithPlayers(val playerDataEntities: List<PlayerDataEntity>, override val game: Game) :
        GameDetailViewState(game)


    data class ClosedGame(val playerDataEntities: List<PlayerDataEntity>, override val game: Game) :
        GameDetailViewState(game)
}