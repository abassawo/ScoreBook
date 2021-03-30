package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.raw.Game

class Greeting {
    fun greeting(): String {
        return "${Platform().platform}!"
    }

    fun games(): List<Game> {
        return listOf(Game(name = "Test game", dateCreated = Date().getTime()))
    }
}