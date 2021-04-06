package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.raw.Player

object ObjectConverter {

    fun convertPlayersStringToList(text: String) {
//        return Gson().fromJson(playerString, playerListType)
    }

    fun convertListToString(players: List<Player>) {
        val map = mapOf<Int, String>()
    }
}
