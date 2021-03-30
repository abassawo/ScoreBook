package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.shared.common.Environment
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailEngine
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.entities.GameDetailViewEvent
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.entities.GameDetailViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor() : ViewModel() {
    private lateinit var engine: GameDetailEngine

    val viewState: LiveData<GameDetailViewState> by lazy {
        engine.viewState.asLiveData(viewModelScope.coroutineContext) }
    val viewEvent: LiveData<GameDetailViewEvent> by lazy {
        engine.viewEvent.asLiveData(viewModelScope.coroutineContext) }

    fun launch(gameId: Long) {
        viewModelScope.launch {
            val game = Environment.appRepository.getGame(gameId)
            engine = GameDetailEngine(viewModelScope, game)
        }
    }

    fun refreshScore() = engine.refreshScore()

    fun handleInteraction(interaction: GameDetailInteraction) =
        engine.handleInteraction(interaction)

    fun navigateToAddPlayerPage() = engine.navigateToAddPlayerPage()
}