package com.lindenlabs.scorebook.shared.common.entities.home

import com.lindenlabs.scorebook.shared.common.raw.Game

data class HomeViewState(val entities: List<GameRowEntity>)

sealed class GameRowEntity {
    data class HeaderType(val title: String) : GameRowEntity()

    data class BodyType(
        val game: Game,
        val clickAction: (interaction: HomeInteraction.GameClicked) -> Unit,
        val swipeAction: (interaction: HomeInteraction.SwipeToDelete) -> Unit
    ) : GameRowEntity()
}

sealed class HomeViewEvent {
    object None : HomeViewEvent()

    object ShowWelcomeScreen : HomeViewEvent()

    object DismissWelcomeMessage : HomeViewEvent()

    data class AlertNoTextEntered(val errorText: String = "Enter a game name to begin") : HomeViewEvent()

    data class ShowAddPlayersScreen(val game: Game) : HomeViewEvent()

    data class ShowGameDetail(val game: Game) : HomeViewEvent()

    class ShowUndoDeletePrompt(val game: Game, val restoreIndex: Int) : HomeViewEvent()
}

sealed class HomeInteraction {
    object Refresh : HomeInteraction()
    data class GameDetailsEntered(val name: String?, val lowestScoreWins: Boolean = false) : HomeInteraction()
    data class GameClicked(val game: Game) : HomeInteraction()
    data class SwipeToDelete(val game: Game) : HomeInteraction()
    data class UndoDelete(val game: Game, val restoreIndex: Int) : HomeInteraction()
    object DismissWelcome : HomeInteraction()
}

