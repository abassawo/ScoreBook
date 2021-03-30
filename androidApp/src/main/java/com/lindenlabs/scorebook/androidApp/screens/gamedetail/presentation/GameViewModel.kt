package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Environment
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailEngine
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewEvent
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor(gameId: String) : ViewModel() {
    private val engine: GameDetailEngine = GameDetailEngine(viewModelScope)

    val viewState: LiveData <GameDetailViewState> =
        engine.viewState.asLiveData(viewModelScope.coroutineContext)
    val viewEvent: LiveData<GameDetailViewEvent> =
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext)

    fun launch(gameId: String) {
        viewModelScope.launch {
            val game = Environment.appRepository.getGame(gameId)
            engine.launch(game)
        }
    }

    fun refreshScore() = engine.refreshScore()

    fun handleInteraction(interaction: GameDetailInteraction) =
        engine.handleInteraction(interaction)

    fun navigateToAddPlayerPage() = engine.navigateToAddPlayerPage()
}