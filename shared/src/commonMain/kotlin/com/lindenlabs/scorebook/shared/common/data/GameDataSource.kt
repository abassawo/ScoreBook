package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

class GameDataSource : DataSource<Game> {
    val games: MutableList<Game> = mutableListOf()
    override var items: MutableList<Game> = games

    override suspend fun load(): List<Game> {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Long): Game {
        TODO("Not yet implemented")
    }

    override suspend fun store(t: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun update(t: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(t: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }
}