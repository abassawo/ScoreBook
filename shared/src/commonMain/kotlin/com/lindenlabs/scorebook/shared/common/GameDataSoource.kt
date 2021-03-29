package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Game

class GameDataSoource : DataSource<Game> {
    override var items: MutableList<Game>
        get() = TODO("Not yet implemented")
        set(value) {}

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