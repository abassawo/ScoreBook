package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeInteraction

sealed class GameRowEntity {
    data class HeaderType(val title: String) : GameRowEntity()

    data class BodyType(
        val game: Game,
        val clickAction: (interaction: HomeInteraction.GameClicked) -> Unit,
        val swipeAction: (interaction: HomeInteraction.SwipeToDelete) -> Unit
    ) : GameRowEntity()
}