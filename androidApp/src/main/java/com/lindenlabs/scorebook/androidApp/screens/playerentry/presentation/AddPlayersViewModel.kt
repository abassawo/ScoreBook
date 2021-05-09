package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import androidx.lifecycle.*
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayerInteraction
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayersViewEvent
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayersViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import com.lindenlabs.scorebook.shared.common.raw.toText
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AddPlayersViewModel(val appRepository: AppRepository, val gameId: String) : ViewModel() {
    private lateinit var currentGame: Game
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<Event<AddPlayersViewEvent>> = MutableLiveData()

    init {
        launch(gameId)
    }

    fun launch(gameId: String) {
        viewModelScope.launch {
            currentGame = appRepository.getGame(gameId)
            populateAutocompleteAdapter()
            showPlayers(currentGame.players)
        }
    }

    private fun populateAutocompleteAdapter() {
        viewModelScope.launch {
            runCatching { appRepository.getPlayers() - currentGame.players }
                .onSuccess { players ->
                    viewState.value =
                        AddPlayersViewState.LoadAutocompleteAdapter(players.map { it.name })
                }
                .onFailure { }
        }
    }

    private fun showPlayers(players: List<Player>) {
        if (players.isNotEmpty())
            viewState.value = AddPlayersViewState.UpdateCurrentPlayersText(players.toText())
    }


    fun handleInteraction(interaction: AddPlayerInteraction) =
        when (interaction) {
            is AddPlayerInteraction.SavePlayerDataAndExit -> savePlayerDataAndExit()
            is AddPlayerInteraction.AddAnotherPlayer -> addAnotherPlayer(interaction.playerName)
            is AddPlayerInteraction.TextEntered -> viewState.value =
                AddPlayersViewState.PlusButtonEnabled(isEnabled = true)
            is AddPlayerInteraction.EmptyText -> viewState.value =
                AddPlayersViewState.PlusButtonEnabled(isEnabled = false)
            is AddPlayerInteraction.Typing -> viewState.value = AddPlayersViewState.TypingState
            is AddPlayerInteraction.GoBackHome -> navigateHome()
        }

    private fun navigateHome() {
        viewModelScope.launch {
            appRepository.updateGame(currentGame)
        }
        viewEvent.value = Event(AddPlayersViewEvent.NavigateHome)
    }

    private fun addAnotherPlayer(playerName: String) {
        if (playerName.isEmpty())
            viewState.value = AddPlayersViewState.TextEntryError
        else {
            val player = Player(playerName, scoreTotal = 0)
            currentGame.players += player
            viewModelScope.launch {
                appRepository.addPlayer(player)
            }
            val playersText = currentGame.players.toText()
            viewState.value = AddPlayersViewState.UpdateCurrentPlayersText(playersText)
        }
    }

    private fun savePlayerDataAndExit() {
        if (currentGame.players.isEmpty()) {
            viewState.value = AddPlayersViewState.TextEntryError
        } else {
            // navigate to Game Detail screen
            viewModelScope.launch {
                runCatching { appRepository.updateGame(currentGame) }
                    .onSuccess {
                        navigateToGameDetail()
                    }
                    .onFailure { Timber.e(it) }
            }
        }
    }

    private fun navigateToGameDetail() =
        viewEvent.postValue(Event(AddPlayersViewEvent.NavigateToGameDetail(currentGame)))
}