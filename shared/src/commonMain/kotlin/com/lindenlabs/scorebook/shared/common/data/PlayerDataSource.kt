package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Player
import comlindenlabsscorebooksharedcommon.PlayerHistoryQueries
import comlindenlabsscorebooksharedcommon.Players

class PlayerDataSource(private val playerHistoryQueries: PlayerHistoryQueries) :
    PlayerHistoryQueries by playerHistoryQueries
//    val players: MutableList<Player> = mutableListOf()
//
//    override suspend fun load(): List<Player> = mutableListOf<Player>().also { players ->
//        players.addAll(playerHistoryQueries.selectAll().executeAsList().map { player ->
//            Player(
//                name = player.name,
//                scoreTotal = player.scoreTotal.toInt(),
//                isPlayerTurn = player.isPlayerTurn ?: false
//            )
//        })
//    }
//
//    override suspend fun get(id: String): Player {
//        val query = playerHistoryQueries.selectById(id).executeAsOneOrNull()
//        val player: Players = requireNotNull(query)
//        return Player(
//            id = player.id,
//            name = player.name,
//            dateCreated = player.dateCreated
//        )
//    }
//
//    override suspend fun store(player: Player) = with(player) {
//        playerHistoryQueries.insertOrReplace(
//            name = name,
//            id = id,
//            dateCreated = dateCreated,
//            isPlayerTurn = isPlayerTurn,
//            scoreTotal = scoreTotal.toLong(),
//            rounds = ""
//        )
//    }
//
//    override suspend fun update(player: Player) = store(player)
//
//    override suspend fun delete(player: Player) = playerHistoryQueries.deleteByLabel(player.id)
//
//    override suspend fun clear() = playerHistoryQueries.empty()
//}