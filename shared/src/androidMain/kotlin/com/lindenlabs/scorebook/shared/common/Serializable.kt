package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player

//actual typealias Serializable = kotlinx.serialization.Serializable

actual class Json  {

    actual fun playerToString(players: List<Player>): String {
//        val format = Json { }
//        return format.encodeToString(players)
        return ""
    }

    actual fun stringToPlayers(playerString: String): List<Player> {
//        val format = Json { }
//       return format.decodeFromString(playerString)
       return emptyList()
    }

}