package com.lindenlabs.scorebook.androidApp.screens.home.data.model

data class Player(val name: String, val scoreTotal: Int = 0, val rounds: List<Round> = emptyList()  )