package com.lindenlabs.scorebook.shared.common.engines.gamedetail

import com.lindenlabs.scorebook.shared.common.engines.BaseInteraction
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

class GameViewEntityMapper {

    fun map(players: List<Player>, interaction: (GameDetailInteraction)-> Unit) : List<PlayerDataEntity>{
        return players.map{  player ->
            PlayerDataEntity(player, { interaction(it) })
        }
    }
}

sealed class GameDetailViewState {

    object Loading : GameDetailViewState()

    sealed class WithGameData(open val game: Game) : GameDetailViewState() {

        data class NotStarted(override val game: Game) : WithGameData(game)


        data class StartedWithPlayers(
            val playerDataEntities: List<PlayerDataEntity>,
            override val game: Game
        ) : WithGameData(game)


        data class ClosedGame(val playerDataEntities: List<PlayerDataEntity>, override val game: Game) : WithGameData(game)
    }
}

sealed class GameDetailViewEvent {

    object None : GameDetailViewEvent()

    data class AddPlayersClicked(val game: Game) : GameDetailViewEvent()

    data class EditScoreForPlayer(val game: Game, val player: Player) : GameDetailViewEvent()

    data class PromptToRestartGame(val game: Game) : GameDetailViewEvent()

    object GoBackHome : GameDetailViewEvent()

    object ConfirmEndGame : GameDetailViewEvent()

    data class EndGame(val game: Game) : GameDetailViewEvent()

    data class ShowRestartingGameMessage(val game: Game) : GameDetailViewEvent()

    data class NavigateToEditHome(val game: Game) : GameDetailViewEvent()
}

sealed class GameDetailInteraction : BaseInteraction {
    object AddPlayerClicked : GameDetailInteraction()
    object RefreshScores : GameDetailInteraction()
    data class PlayerClicked(val player : Player) : GameDetailInteraction()
    object GoBack : GameDetailInteraction()
    object EndGameClicked : GameDetailInteraction()
    object EndGameConfirmed : GameDetailInteraction()
    object RestartGameClicked : GameDetailInteraction()
    object EditGameClicked : GameDetailInteraction()
}

data class PlayerDataEntity(
    val player: Player,
    val clickAction: (interaction: GameDetailInteraction) -> Unit,
    val isPlayersTurn: Boolean = player.isPlayerTurn,
)