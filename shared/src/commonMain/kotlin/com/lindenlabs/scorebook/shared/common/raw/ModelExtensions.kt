package com.lindenlabs.scorebook.shared.common.raw

fun List<String>.toSpaceSeparatedText(): String {
    val list = this
    return buildString {
        list.forEach {
            append("$it ")
        }
    }
}