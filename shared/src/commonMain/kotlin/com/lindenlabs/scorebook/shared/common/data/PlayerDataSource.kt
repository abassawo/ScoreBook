package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Player
import comlindenlabsscorebooksharedcommon.PlayerHistoryQueries

class PlayerDataSource(playerHistoryQueries: PlayerHistoryQueries) : DataSource<Player> {
    val players: MutableList<Player> = mutableListOf()
    override var items: MutableList<Player> = players

    override suspend fun load(): List<Player> = items

    override suspend fun get(id: String): Player {
        return items.find { it.id == id } ?: throw IllegalStateException()
    }

    override suspend fun store(t: Player) {
        this.players += t
    }

    override suspend fun update(t: Player) {
        val found = players.find { it.id == t.id }
        players.set(index = players.indexOf(found), t)
    }

    override suspend fun delete(t: Player) {
        players.remove(t)
    }

    override suspend fun clear() {
        players.clear()
    }
}