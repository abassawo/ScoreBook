package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual typealias Serializable = kotlinx.serialization.Serializable

actual class Json  {

    actual fun playerToString(players: List<Player>): String {
        val format = Json { }
        return format.encodeToString(players)
    }

    actual fun stringToPlayers(playerString: String): List<Player> {
        val format = Json { }
       return format.decodeFromString(playerString)
    }

}