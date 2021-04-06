package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player

object PlayerListConverter {
    fun playerToString(players: List<Player>): String = ""

    fun stringToPlayers(playerString: String): List<Player> {
//        Timber.e("Player string $playerString")
//        val playerListType: Type = object : TypeToken<ArrayList<Player?>?>() {}.type
        return emptyList()
    }
}
