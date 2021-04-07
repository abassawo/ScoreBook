package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player

actual class Json actual constructor(action: () -> Unit){
    actual fun playerToString(players: List<Player>): String {
        return ""
    }

    actual fun stringToPlayers(playerString: String): List<Player> {
        return emptyList()
    }
}