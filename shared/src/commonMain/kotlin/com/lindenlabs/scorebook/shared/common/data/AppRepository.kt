package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.DefaultDispatcherProvider
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import com.lindenlabs.scorebook.shared.common.raw.Player
import comlindenlabsscorebooksharedcommon.GameHistoryQueries
import comlindenlabsscorebooksharedcommon.PlayerHistoryQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppRepository(
    private val gameHistoryQueries: GameHistoryQueries,
    private val playerHistoryQueries: PlayerHistoryQueries,
    val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default(),
) {
    suspend fun loadGames(): List<Game> {
        val games = mutableListOf<Game>().also { games ->
            games.addAll(gameHistoryQueries.selectAll().executeAsList().map { game ->
                Game(
                    id = game.id,
                    name = game.name,
                    dateCreated = game.dateCreated,
                    isClosed = game.isClosed ?: false,
                    strategy = GameStrategy.valueOf(game.strategy),
                    playerIds = game.playerIds.split(" ")
                )
            })
        }

        for (game in games) {
            game.players += playerHistoryQueries.selectById(game.id).executeAsList().map {
                Player(
                    name = it.name,
                    scoreTotal = it.scoreTotal.toInt(),
                    isPlayerTurn = it.isPlayerTurn ?: false,
                    id = it.id,
                    dateCreated = it.dateCreated
                )
            }
        }
        return games
    }

    fun getGame(id: String): Game {
        val game = requireNotNull(gameHistoryQueries.selectById(id).executeAsOneOrNull())
        return Game(
            id = game.id,
            name = game.name,
            dateCreated = game.dateCreated,
            isClosed = game.isClosed ?: false,
            strategy = GameStrategy.valueOf(game.strategy),
            playerIds = emptyList() // todo
        )
    }

    suspend fun storeGame(game: Game) = withContext(dispatcher) {
        val playerText = buildString {
            game.players.forEach {
                append(it.id + " ")
            }
        }

        gameHistoryQueries.insertOrReplace(
            game.id,
            game.name,
            game.dateCreated,
            game.isClosed,
            game.strategy.name,
            playerText
        )
    }

    suspend fun updateGame(t: Game) = withContext(dispatcher) {
        gameHistoryQueries.insertOrReplace(
            t.id,
            t.name,
            t.dateCreated,
            t.isClosed,
            t.strategy.name,
            t.playerIds.toText()
        )
    }

    private fun List<String>.toText(): String {
        return buildString {
            this.forEach {
                append("$it ")
            }
        }
    }

    suspend fun deleteGame(t: Game) =
        withContext(dispatcher) { gameHistoryQueries.deleteByLabel(t.id) }

    suspend fun clearGame() = withContext(dispatcher) { gameHistoryQueries.empty() }


    //
//    suspend fun load(): List<Game> = withContext(dispatcher) {
//        gameDataSource.load()
//    }
//
//    suspend fun storeGame(game: Game) =
//        withContext(dispatcher) {
//            gameDataSource.store(game)
//            game.playerIds.forEach { player ->
//                addPlayer(
//                    player)
//            }
//        }
//
//    suspend fun updateGame(game: Game) = storeGame(game)
//
//    suspend fun deleteGame(game: Game) = withContext(dispatcher) {
//        gameDataSource.delete(game)
//    }
//
    suspend fun getPlayers(): List<Player> = withContext(dispatcher) {
        playerHistoryQueries.selectAll().executeAsList().map {
            Player(
                name = it.name,
                scoreTotal = it.scoreTotal.toInt(),
                isPlayerTurn = it.isPlayerTurn ?: false,
                id = it.id,
                dateCreated = it.dateCreated
            )
        }
    }

    suspend fun addPlayer(player: Player) = withContext(dispatcher) {
        val playersInDB = getPlayers()

        if (playersInDB.find { it.name == player.name } == null) {
            playerHistoryQueries.insertOrReplace(
                name = player.id,
                scoreTotal = player.scoreTotal.toLong(),
                rounds = "",
                isPlayerTurn = player.isPlayerTurn,
                id = player.id,
                dateCreated = player.dateCreated
            )
        }
    }

    suspend fun getPlayer(playerId: String): Player = withContext(dispatcher) {
        val result = requireNotNull(playerHistoryQueries.selectById(playerId).executeAsOneOrNull())
        Player(
            name = result.name,
            scoreTotal = result.scoreTotal.toInt(),
            isPlayerTurn = result.isPlayerTurn ?: false,
            id = result.id,
            dateCreated = result.dateCreated
        )
    }
}