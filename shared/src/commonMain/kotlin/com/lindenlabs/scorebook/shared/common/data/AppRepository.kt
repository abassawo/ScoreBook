//package com.lindenlabs.scorebook.shared.common.data
//
//import com.lindenlabs.scorebook.shared.common.raw.Game
//import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
//import com.lindenlabs.scorebook.shared.common.raw.Player
//import comlindenlabsscorebooksharedcommon.GameHistoryQueries
//import comlindenlabsscorebooksharedcommon.PlayerHistoryQueries
//import kotlinx.coroutines.flow.MutableStateFlow
//
//class AppRepository(database: AppDatabase) {
//    private val gameHistoryQueries: GameHistoryQueries = database.gameHistoryQueries
//    private val playerHistoryQueries: PlayerHistoryQueries = database.playerHistoryQueries
//
//    suspend fun storeDummyGame() = storeGame(Game(name = "Testing"))
//
//    suspend fun loadGames(): List<Game> =
//            gameHistoryQueries.selectAll().executeAsList().map { game ->
//                Game(
//                    id = game.id,
//                    name = game.name,
//                    dateCreated = game.dateCreated,
//                    isClosed = game.isClosed ?: false,
//                    strategy = GameStrategy.valueOf(game.strategy),
//                    players = PlayerListConverter.stringToPlayers(game.players).toMutableList()
//                )
//            }
//
//
//    suspend fun getGame(id: String): Game {
//        return loadGames().find { it.id == id }!!
//    }
//
//    suspend fun storeGame(game: Game) = gameHistoryQueries.insertOrReplace(
//        id = game.id,
//        name = game.name,
//        dateCreated = game.dateCreated,
//        isClosed = game.isClosed,
//        strategy = game.strategy.name,
//        players = PlayerListConverter.playerToString(game.players)
//    )
//
//    suspend fun updateGame(game: Game) = gameHistoryQueries.insertFullGameObject(game.toDao())
//
//    suspend fun deleteGame(t: Game) = gameHistoryQueries.deleteByLabel(t.id)
//
//    suspend fun clearGames() = gameHistoryQueries.empty()
//
//    suspend fun getPlayers(): List<Player> {
//        return playerHistoryQueries.selectAll().executeAsList().map { player ->
//            Player(
//                name = player.name,
//                scoreTotal = player.scoreTotal.toInt(),
//                isPlayerTurn = player.isPlayerTurn ?: false,
//                id = player.id,
//                dateCreated = player.dateCreated
//            )
//        }
//    }
//
//
//    suspend fun addPlayer(player: Player) {
//        if (getPlayer(player.id) == null) {
//            playerHistoryQueries.insertOrReplace(
//                name = player.name,
//                scoreTotal = player.scoreTotal.toLong(),
//                rounds = "",
//                isPlayerTurn = player.isPlayerTurn,
//                id = player.id,
//                dateCreated = player.dateCreated
//            )
//        }
//    }
//
//    suspend fun getPlayer(playerId: String): Player? {
//        return getPlayers().find { it.id == playerId }
//    }
//}
//
