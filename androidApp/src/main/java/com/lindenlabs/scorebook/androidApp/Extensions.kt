package com.lindenlabs.scorebook.androidApp

import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

fun List<Player>.toText(): String {
    if (this.isEmpty()) return ""

    fun makeCommaText(items: List<Player>): String = buildString {
        append(items.first().name)
        for (i in 1 until items.size) {
            append(", ${items[i].name}")
        }
    }

    return when (this.size) {
        0 -> ""
        1 -> this.first().name
        else -> makeCommaText(this)
    }
}