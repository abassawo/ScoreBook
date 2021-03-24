package com.lindenlabs.scorebook.shared

class Greeting {
    fun greeting(): String {
        return "${Platform().platform}!"
    }
}