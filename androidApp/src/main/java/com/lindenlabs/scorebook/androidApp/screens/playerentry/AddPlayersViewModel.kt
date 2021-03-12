package com.lindenlabs.scorebook.androidApp.screens.playerentry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.Launchable
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersViewState.*
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddPlayersViewModel @Inject constructor(
    val environment: Environment,
    args: AddPlayersFragmentArgs,
    coroutineScope: CoroutineScope? = null
) : ViewModel(), Launchable {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private val currentGame: Game = args.gameArg


    init {
        launch()
    }

    override fun launch() {
        populateAutocompleteAdapter()
        showPlayers(currentGame.players)
    }

    private fun populateAutocompleteAdapter() =
        viewModelScope.launch {
            val games = environment.load()
            val setOfNames: MutableSet<String> = mutableSetOf()
            games.map { it.players.map { player -> setOfNames += player.name } }
            viewState.postValue(LoadAutocompleteAdapter(setOfNames.toList()))
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
        viewModelScope.launch { environment.updateGame(currentGame) }
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
                environment.updateGame(currentGame)
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