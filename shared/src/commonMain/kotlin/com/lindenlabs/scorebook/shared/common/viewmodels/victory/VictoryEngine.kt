package com.lindenlabs.scorebook.shared.common.viewmodels.victory

import com.lindenlabs.scorebook.shared.common.data.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VictoryEngine(private val coroutineScope: CoroutineScope, val appRepository: AppRepository) {
    val viewState: MutableStateFlow<VictoryState> = MutableStateFlow(VictoryState(""))
    val viewEvent: MutableStateFlow<VictoryViewEvent> = MutableStateFlow(VictoryViewEvent.None)


    fun launch(gameId: String) {
        coroutineScope.launch {
            val game = requireNotNull(appRepository.getGame(gameId))
            viewState.value = VictoryState(victoryText = game.end())
            appRepository.updateGame(game)
        }
    }

    fun handleInteraction(interaction: VictoryInteraction) {
        when (interaction) {
            is VictoryInteraction.GoHome -> Unit // viewEvent.value = VictoryViewEvent.GoHome
        }
    }
}