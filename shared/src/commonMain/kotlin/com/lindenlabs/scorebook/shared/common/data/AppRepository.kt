package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.DefaultDispatcherProvider
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import com.lindenlabs.scorebook.shared.common.raw.Player
import comlindenlabsscorebooksharedcommon.GameHistoryQueries
import comlindenlabsscorebooksharedcommon.PlayerHistoryQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppRepository(database: AppDatabase) {
    private val gameHistoryQueries: GameHistoryQueries = database.gameHistoryQueries
    private val playerHistoryQueries: PlayerHistoryQueries = database.playerHistoryQueries
    private var games: MutableList<Game> = mutableListOf()
    private var players: MutableList<Player> = mutableListOf()

    val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().default()

    suspend fun storeDummyGame() =
        storeGame(Game(name = "Testing"))

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
        games
    }

    suspend fun getGame(id: String): Game {
        loadGames()
        return games.find { it.id == id }!!
    }

    suspend fun storeGame(game: Game) = withContext(dispatcher) {
        val playerText = PlayerListConverter.playerToString(game.players)

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
}