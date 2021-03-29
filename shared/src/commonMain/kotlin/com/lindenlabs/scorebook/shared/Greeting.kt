package com.lindenlabs.scorebook.shared

import com.lindenlabs.scorebook.shared.raw.Game


class Greeting {
    fun greeting(): String {
        return "${Platform().platform}!"
    }

    fun games(): List<Game> {
        return listOf(Game(name = "Test game"))
    }
}