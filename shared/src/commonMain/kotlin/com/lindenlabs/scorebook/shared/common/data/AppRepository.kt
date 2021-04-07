package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.DefaultDispatcherProvider
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import com.lindenlabs.scorebook.shared.common.raw.Player
import comlindenlabsscorebooksharedcommon.GameHistoryQueries
import comlindenlabsscorebooksharedcommon.Games
import comlindenlabsscorebooksharedcommon.PlayerHistoryQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppRepository(
    private val gameHistoryQueries: GameHistoryQueries,
    private val playerHistoryQueries: PlayerHistoryQueries,
    val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default(),
) {

    private var games: MutableList<Game> = mutableListOf()
    private var players: MutableList<Player> = mutableListOf()

    suspend fun loadGames(): List<Game> = withContext(dispatcher) {
        games = mutableListOf<Game>().also { games ->
            games.addAll(gameHistoryQueries.selectAll().executeAsList().map { game ->
                Game(
                    id = game.id,
                    name = game.name,
                    dateCreated = game.dateCreated,
                    isClosed = game.isClosed ?: false,
                    strategy = GameStrategy.valueOf(game.strategy),
                    players = PlayerListConverter.stringToPlayers(game.players).toMutableList()
                )
            })
        }
        games.forEach { game ->
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
        games
    }

    suspend fun getGame(id: String): Game {
        loadGames()
        return games.find { it.id == id }!!
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

    suspend fun updateGame(game: Game) {
        games[games.indexOf(game)] = game
        withContext(dispatcher) {
            gameHistoryQueries.insertFullGameObject(game.toDao())
        }
    }

    suspend fun deleteGame(t: Game) =
        withContext(dispatcher) { gameHistoryQueries.deleteByLabel(t.id) }

    suspend fun clearGames() = withContext(dispatcher) { gameHistoryQueries.empty() }

    suspend fun getPlayers(): List<Player> = withContext(dispatcher) {
        players = mutableListOf<Player>().also { players ->
            players.addAll(playerHistoryQueries.selectAll().executeAsList().map { player ->
                Player(
                    name = player.name,
                    scoreTotal = player.scoreTotal.toInt(),
                    isPlayerTurn = player.isPlayerTurn ?: false,
                    id = player.id,
                    dateCreated = player.dateCreated
                )
            })
        }
        players
    }

    suspend fun addPlayer(player: Player) = withContext(dispatcher) {
        if (players.find{ it.name == player.name } == null) {
            playerHistoryQueries.insertOrReplace(
                name = player.name,
                scoreTotal = player.scoreTotal.toLong(),
                rounds = "",
                isPlayerTurn = player.isPlayerTurn,
                id = player.id,
                dateCreated = player.dateCreated
            )
        }
    }

    suspend fun getPlayer(playerId: String): Player? = withContext(dispatcher) {
        getPlayers()
        players.find { it.id == playerId }
    }

    suspend fun syncGameWithPlayers(game: Game, playerIds: List<String>) {
        for (id in playerIds) {
            val player = getPlayer(id)
            player?.let { game.players += it }
        }
    }
}