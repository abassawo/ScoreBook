//package com.lindenlabs.scorebook.androidApp.base.data.sources
//
//import com.lindenlabs.scorebook.androidApp.base.data.persistence.PlayerStore
//import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
//import java.util.*
//
//class LocalPlayerDataSource(private val playersDao: PlayerStore) : PlayerDataSource {
//    var players: MutableList<Player> = mutableListOf()
//
//    fun players() = players
//
//    override suspend fun load() = playersDao.loadAll()
//
//    override suspend fun get(id: UUID): Player? = players.find { it.id == id }
//
//    override suspend fun store(player: Player) = playersDao.insert(player)
//
//    override suspend fun update(player: Player) = playersDao.update(player)
//
//    override suspend fun delete(player: Player) = playersDao.delete(player)
//
//    override suspend fun clear() = playersDao.delete(*players.toTypedArray())
//}