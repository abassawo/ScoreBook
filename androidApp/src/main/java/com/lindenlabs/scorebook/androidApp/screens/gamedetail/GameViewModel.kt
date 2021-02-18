package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import java.util.*

class GameViewModel : ViewModel() {
    val viewState: MutableLiveData<GameViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<GameViewEvent> = MutableLiveData()
    private val repository: GameDataSource = GameRepository

    private var game: Game? = null

    private var isFirstRun: Boolean = true // todo - manage with shared prefs

    fun launch(gameId: UUID) {
        val game = repository.getGameById(gameId)
        game?.let {
            this.game = it

            if (isFirstRun && it.players.isNullOrEmpty()) {
                isFirstRun = false
                viewEvent.postValue(GameViewEvent.AddPlayersClicked(it)) // Bypass home screen, just add
            }
            else if(it.players.isNullOrEmpty()) {
                viewState.postValue(GameViewState.EmptyState)
            } else {
                viewState.postValue(GameViewState.GameStarted(it, it.players))
            }
        }
    }

    fun navigateToAddPlayerPage() {
        game?.let { viewEvent.postValue(AddPlayersClicked(it)) }
    }
}