package com.lindenlabs.scorebook.shared

class HomeScreenProvider {
}

sealed class ScreenState {
    data class HomeViewState(val entities: List<GameRowEntity>)
}