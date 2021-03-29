package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Player

class PlayerDataSource : DataSource<Player> {
    val players: MutableList<Player> = mutableListOf()
    override var items: MutableList<Player> = players

    override suspend fun load(): List<Player> = items

    override suspend fun get(id: Long): Player {
        return items.find { it.id == id } ?: throw IllegalStateException()
    }

    override suspend fun store(t: Player) {

    }

    override suspend fun update(t: Player) {
    }

    override suspend fun delete(t: Player) {
    }

    override suspend fun clear() {
    }
}