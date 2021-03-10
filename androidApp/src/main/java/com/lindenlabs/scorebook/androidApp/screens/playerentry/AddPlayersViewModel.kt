package com.lindenlabs.scorebook.androidApp.screens.playerentry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddPlayersViewModel : ViewModel() {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private lateinit var game: Game
    private lateinit var environment: Environment


    fun launch(environment: Environment, args: AddPlayersFragmentArgs) {
        this.environment = environment
        viewModelScope.launch {
            runBlocking {
                val games = environment.load()
                val setOfNames: MutableSet<String> = mutableSetOf()
                games.map { it.players.map { player -> setOfNames += player.name } }
                viewState.postValue(AddPlayersViewState.InitialState(setOfNames.toList()))
            }
        }

        this.game = args.gameArg
        val players = game.players
        if (players.isNotEmpty()) {
            viewState.postValue(AddPlayersViewState.UpdateCurrentPlayersText(players.toText()))
        }
    }

    fun handleInteraction(interaction: AddPlayerInteraction) {
        when (interaction) {
            is SavePlayerDataAndExit -> {
                if (game.players.isEmpty()) {
                    viewState.postValue(AddPlayersViewState.TextEntryError)
                } else {
                    viewEvent.postValue(AddPlayersViewEvent.NavigateToGameDetail(game))
                    viewModelScope.launch {
                        runBlocking { environment.updateGame(game) }
                    }
                }
                // navigate to Game Detail screen
            }
            is AddAnotherPlayer -> {
                // stay on same screen
                if (interaction.playerName.isEmpty())
                    viewState.postValue(AddPlayersViewState.TextEntryError)
                else {
                    val player = Player(interaction.playerName)
                    game.players += player
                    val playersText = game.players.toText()
                    viewState.postValue(AddPlayersViewState.UpdateCurrentPlayersText(playersText))
                }
            }
            is TextEntered -> viewState.postValue(AddPlayersViewState.ValidateTextForPlusButton(true))
            is EmptyText -> viewState.postValue(AddPlayersViewState.ValidateTextForPlusButton(false))
            is Typing -> viewState.postValue(AddPlayersViewState.TypingState)
            GoBackHome -> {
                viewModelScope.launch  { environment.updateGame(game) }
                viewEvent.postValue(AddPlayersViewEvent.NavigateHome)
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