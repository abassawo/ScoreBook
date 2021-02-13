package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetClosedGames
import com.lindenlabs.scorebook.androidApp.screens.home.domain.GetOpenGames

class HomeViewModel(getOpenGames: GetOpenGames, getClosedGames: GetClosedGames) : ViewModel() {
    val viewState: MutableLiveData<HomeViewState> = MutableLiveData()

    init {
        val state = HomeViewState.GamesState(
            openGames = getOpenGames(),
            closedGames = getClosedGames()
        )
        viewState.postValue(state)
    }
}