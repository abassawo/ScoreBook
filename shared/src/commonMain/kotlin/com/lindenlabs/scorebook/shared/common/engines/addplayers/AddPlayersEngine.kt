package com.lindenlabs.scorebook.shared.common.engines.addplayers

import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersViewState.*
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import com.lindenlabs.scorebook.shared.common.raw.toText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPlayersEngine(private val coroutineScope: CoroutineScope, private val appRepository: AppRepository) {
    private lateinit var currentGame: Game
    val viewState: MutableStateFlow<AddPlayersViewState> = MutableStateFlow(None)
    val viewEvent: MutableStateFlow<Event<AddPlayersViewEvent>> = MutableStateFlow(Event(AddPlayersViewEvent.None))

    fun launch(gameId: String) {
        coroutineScope.launch {
            currentGame = appRepository.getGame(gameId)
            populateAutocompleteAdapter()
            showPlayers(currentGame.players)
        }
    }

    private fun populateAutocompleteAdapter() {
        coroutineScope.launch {
            runCatching { appRepository.getPlayers() - currentGame.players}
                .onSuccess { players ->
                    viewState.value = LoadAutocompleteAdapter(players.map {  it.name })
                }
                .onFailure { }
        }
    }

    private fun showPlayers(players: List<Player>) {
        if (players.isNotEmpty())
            viewState.value = UpdateCurrentPlayersText(players.toText())
    }


    fun handleInteraction(interaction: AddPlayerInteraction) =
        when (interaction) {
            is AddPlayerInteraction.SavePlayerDataAndExit -> savePlayerDataAndExit()
            is AddPlayerInteraction.AddAnotherPlayer -> addAnotherPlayer(interaction.playerName)
            is AddPlayerInteraction.TextEntered -> viewState.value =
                PlusButtonEnabled(isEnabled = true)
            is AddPlayerInteraction.EmptyText -> viewState.value =
                PlusButtonEnabled(isEnabled = false)
            is AddPlayerInteraction.Typing -> viewState.value = TypingState
            is AddPlayerInteraction.GoBackHome -> navigateHome()
        }

    private fun navigateHome() {
        coroutineScope.launch {
            appRepository.updateGame(currentGame)
        }
        viewEvent.value = Event(AddPlayersViewEvent.NavigateHome)
    }

    private fun addAnotherPlayer(playerName: String) {
        if (playerName.isEmpty())
            viewState.value = TextEntryError
        else {
            val player = Player(playerName, scoreTotal = 0)
            currentGame.players += player
            coroutineScope.launch {
                appRepository.addPlayer(player)
            }
            val playersText = currentGame.players.toText()
            viewState.value = UpdateCurrentPlayersText(playersText)
        }
    }

    private fun savePlayerDataAndExit() {
        if (currentGame.players.isEmpty()) {
            viewState.value = TextEntryError
        } else {
            // navigate to Game Detail screen

            coroutineScope.launch {
                withContext(appRepository.dispatcher) {
                    appRepository.updateGame(currentGame)
                }
            }

            viewEvent.value = Event(AddPlayersViewEvent.NavigateToGameDetail(currentGame))
        }
    }
}