package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Game

class GameDataSource : DataSource<Game> {
    val games: MutableList<Game> = mutableListOf()

    override var items: MutableList<Game> = games

    override suspend fun load(): List<Game> = games

    override suspend fun get(id: Long): Game {
        return games.find { it.id == id } ?: throw IllegalStateException()
    }

    override suspend fun store(t: Game) {
        games.add(t)
    }

    override suspend fun update(t: Game) {
        val foundGame = games.find { it.id == t.id }
        games.set(index = games.indexOf(foundGame), t)
    }

    override suspend fun delete(t: Game) {
        games.remove(t)
    }

    override suspend fun clear() {
        games.clear()
    }
}