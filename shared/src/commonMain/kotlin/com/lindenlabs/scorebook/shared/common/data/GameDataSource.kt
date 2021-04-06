package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.ObjectConverter
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import com.lindenlabs.scorebook.shared.common.raw.Player
import comlindenlabsscorebooksharedcommon.GameHistoryQueries
import comlindenlabsscorebooksharedcommon.Games

class GameDataSource(private val gameHistoryQueries: GameHistoryQueries) : DataSource<Game> {

    override var items: MutableList<Game> = mutableListOf<Game>().also { games ->
        games.addAll(gameHistoryQueries.selectAll().executeAsList().map { game ->
            Game(
                id = game.id,
                name = game.name,
                dateCreated = game.dateCreated,
                isClosed = game.isClosed ?: false,
                strategy = GameStrategy.valueOf(game.strategy),
                players = listOf(Player(name = "Abass"))
            )
        })
    }

    override suspend fun load(): List<Game> = items

    override suspend fun get(id: String): Game {
        val query = gameHistoryQueries.selectById(id).executeAsOneOrNull()
//        val playerListType: Type = object : TypeToken<ArrayList<Player?>?>() {}.type


        val game: Games = requireNotNull(query)
        return Game(
            id = game.id,
            name = game.name,
            dateCreated = game.dateCreated,
            isClosed = game.isClosed ?: false,
            strategy = GameStrategy.valueOf(game.strategy),
            players = emptyList()
        )
    }

//    internal inline fun <reified T> Gson.fromJsonAsList(json: String) =
//        fromJson<T>(json, object : TypeToken<T>() {}.type)

    override suspend fun store(game: Game) = with(game) {
        gameHistoryQueries.insertOrReplace(id, name, dateCreated, isClosed, strategy.name, "")
    }

    override suspend fun update(t: Game) = store(t)

    override suspend fun delete(t: Game) = gameHistoryQueries.deleteByLabel(t.id)

    override suspend fun clear() = gameHistoryQueries.empty()
}