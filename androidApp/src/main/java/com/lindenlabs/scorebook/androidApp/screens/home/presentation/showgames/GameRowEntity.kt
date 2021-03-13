package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction

sealed class GameRowEntity {
    data class HeaderType(val title: String) : GameRowEntity()

    data class BodyType(
        val game: Game,
        val clickAction: (interaction: GameInteraction.GameClicked) -> Unit,
        val swipeAction: (interaction: GameInteraction.SwipeToDelete) -> Unit
    ) : GameRowEntity()
}