package com.lindenlabs.scorebook.androidApp.base.data.sources

import com.lindenlabs.scorebook.shared.raw.Game
import com.lindenlabs.scorebook.shared.raw.Player
import java.util.*

interface GameDataSource : DataSource<Game>

interface PlayerDataSource : DataSource<Player>

interface DataSource<T> {
    suspend fun load(): List<T>
    suspend fun get(id: UUID) : T?
    suspend fun store(game: T)
    suspend fun update(game: T)
    suspend fun delete(game: T)
    suspend fun clear()
}
