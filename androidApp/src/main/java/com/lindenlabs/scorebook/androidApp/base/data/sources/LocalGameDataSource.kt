package com.lindenlabs.scorebook.androidApp.base.data.sources

import com.lindenlabs.scorebook.androidApp.base.data.persistence.GameStore
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import java.util.*

open class LocalGameDataSource(private val gamesDao: GameStore) : GameDataSource {
    var games: MutableList<Game> = mutableListOf()

    fun games() = games

    override suspend fun load() = gamesDao.loadAll()

    override suspend fun getGameById(id: UUID): Game? = games.find { it.id == id }

    override suspend fun storeGame(game: Game) = gamesDao.insert(game)

    override suspend fun storeGame(index: Int, game: Game) {
        storeGame(game)
    }

    override suspend fun roundPlayed(game: Game, lastPlayer: Player, newScore: Int) =
        gamesDao.update(game)

    override suspend fun updateGame(game: Game) = gamesDao.update(game)

    override suspend fun deleteGame(game: Game) = gamesDao.delete(game)

    override suspend fun clear() = gamesDao.delete(*games.toTypedArray())
}