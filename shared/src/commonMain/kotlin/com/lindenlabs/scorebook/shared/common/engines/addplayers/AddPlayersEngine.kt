package com.lindenlabs.scorebook.shared.common.engines.addplayers

import com.lindenlabs.scorebook.shared.common.Environment
import com.lindenlabs.scorebook.shared.common.engines.addplayers.AddPlayersViewState.*
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import com.lindenlabs.scorebook.shared.common.raw.toText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPlayersEngine(private val coroutineScope: CoroutineScope) {
    private lateinit var currentGame: Game
    private val appRepository = Environment.appRepository
    val viewState: MutableStateFlow<AddPlayersViewState> = MutableStateFlow(UpdateCurrentPlayersText(""))
    val viewEvent: MutableStateFlow<AddPlayersViewEvent> = MutableStateFlow(AddPlayersViewEvent.None)

    init {
        populateAutocompleteAdapter()
    }

    fun launch(gameId: String) {
        coroutineScope.launch {
            currentGame = appRepository.getGame(gameId)
            showPlayers(currentGame.players)
        }
    }

    private fun populateAutocompleteAdapter() {
        coroutineScope.launch {
            runCatching { appRepository.load() }
                .onSuccess {
                    val players = appRepository.getPlayers()
                    viewState.value = LoadAutocompleteAdapter(players.map { it.name })
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
        viewEvent.value = AddPlayersViewEvent.NavigateHome
    }

    private fun addAnotherPlayer(playerName: String) {
        if (playerName.isEmpty())
            viewState.value = TextEntryError
        else {
            val player = Player(playerName)
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
            viewEvent.value = AddPlayersViewEvent.NavigateToGameDetail(currentGame)
            coroutineScope.launch {
                withContext(appRepository.dispatcher) {
                    appRepository.updateGame(currentGame)
                }
            }
        }
    }
}