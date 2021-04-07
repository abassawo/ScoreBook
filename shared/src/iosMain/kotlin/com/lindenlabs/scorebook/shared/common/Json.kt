package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player
import platform.Foundation.NSJSONSerialization

actual class Json {
    actual fun playerToString(players: List<Player>): String {
        return NSJSONSerialization().toString()
    }

    actual fun stringToPlayers(playerString: String): List<Player> {
        return emptyList()
    }
}