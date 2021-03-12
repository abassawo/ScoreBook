package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayersViewState.*
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayersViewEvent
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayersViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddPlayersViewModel @Inject constructor(
    val appRepository: AppRepository,
    args: AddPlayersFragmentArgs
) : ViewModel() {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private val currentGame: Game = args.gameArg


    init {
        populateAutocompleteAdapter()
        showPlayers(currentGame.players)
    }

    private fun populateAutocompleteAdapter() {
        val setOfNames: MutableSet<String> = mutableSetOf()
        viewModelScope.launch {
            runCatching { appRepository.load() }
                .onSuccess { games ->
                    games.map { it.players.map { player -> setOfNames += player.name } }
                    viewState.postValue(LoadAutocompleteAdapter(setOfNames.toList()))
                }
                .onFailure { }
        }
    }

    private fun showPlayers(players: List<Player>) {
        if (players.isNotEmpty())
            viewState.postValue(UpdateCurrentPlayersText(players.toText()))
    }


    fun handleInteraction(interaction: AddPlayerInteraction) =
        when (interaction) {
            is SavePlayerDataAndExit -> savePlayerDataAndExit()
            is AddAnotherPlayer -> addAnotherPlayer(interaction.playerName)
            is TextEntered -> viewState.postValue(PlusButtonEnabled(isEnabled = true))
            is EmptyText -> viewState.postValue(PlusButtonEnabled(isEnabled = false))
            is Typing -> viewState.postValue(TypingState)
            is GoBackHome -> navigateHome()
        }

    private fun navigateHome() {
        viewModelScope.launch {
            appRepository.updateGame(currentGame)
        }
        viewEvent.postValue(AddPlayersViewEvent.NavigateHome)
    }

    private fun addAnotherPlayer(playerName: String) {
        if (playerName.isEmpty())
            viewState.postValue(TextEntryError)
        else {
            val player = Player(playerName)
            currentGame.players += player
            val playersText = currentGame.players.toText()
            viewState.postValue(UpdateCurrentPlayersText(playersText))
        }
    }

    private fun savePlayerDataAndExit() {
        if (currentGame.players.isEmpty()) {
            viewState.postValue(TextEntryError)
        } else {
            // navigate to Game Detail screen
            viewEvent.postValue(AddPlayersViewEvent.NavigateToGameDetail(currentGame))
            viewModelScope.launch {
                withContext(appRepository.dispatcher) {
                    appRepository.updateGame(currentGame)
                }
            }
        }
    }

    private fun List<Player>.toText(): String {
        if (this.isEmpty()) return ""

        fun makeCommaText(items: List<Player>): String = buildString {
            append(items.first().name)
            for (i in 1 until items.size) {
                append(", ${items[i].name}")
            }
        }

        return when (this.size) {
            0 -> ""
            1 -> this.first().name
            else -> makeCommaText(this)
        }
    }
}