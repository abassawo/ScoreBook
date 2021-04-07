package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

actual typealias Serializable = kotlinx.serialization.Serializable

actual class Json  {

    actual fun playerToString(players: List<Player>): String {
        val format = Json(JsonConfiguration.Default)
        return format.encodeToString(players)
    }

    actual fun stringToPlayers(playerString: String): List<Player> {
        val format = Json(JsonConfiguration.Default)
       return format.decodeFromString(playerString)
    }

}