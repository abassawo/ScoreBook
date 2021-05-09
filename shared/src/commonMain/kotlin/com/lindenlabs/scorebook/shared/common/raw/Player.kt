package com.lindenlabs.scorebook.shared.common.raw

import com.lindenlabs.scorebook.shared.common.Serializable
import com.lindenlabs.scorebook.shared.common.data.Date
import com.lindenlabs.scorebook.shared.common.data.Id
import kotlin.collections.List

@Serializable
data class Player(
    val name: String,
    var scoreTotal: Int,
    var isPlayerTurn: Boolean = false,
    val id: String = Id().id,
    val dateCreated: Long = Date().getTime(),
) {

    fun addToScore(score: Int) {
        this.scoreTotal += score
    }

    fun deductFromScore(score: Int) {
        if (score < 0) throw IllegalStateException()
        this.scoreTotal -= score
    }

    fun resetScore() {
        this.scoreTotal = 0
    }
}

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