package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

class GameDataSource : DataSource<Game> {
    val games: MutableList<Game> = mutableListOf()

    override var items: MutableList<Game> = games

    override suspend fun load(): List<Game> = games

    override suspend fun get(id: Long): Game {
        return games.find { it.id == id } ?: throw IllegalStateException()
    }

    override suspend fun store(t: Game) {
    }

    override suspend fun update(t: Game) {
    }

    override suspend fun delete(t: Game) {
    }

    override suspend fun clear() {
    }
}