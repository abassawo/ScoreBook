package com.lindenlabs.scorebook.androidApp.screens.addplayers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.androidApp.base.domain.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction.*

class AddPlayersViewModel(application: Application) : AndroidViewModel(application) {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private val repository: GameDataSource = PersistentGameRepository.getInstance(application)
    private lateinit var game: Game

    init {
        repository.load {
            val setOfNames: MutableSet<String> = mutableSetOf()
            val (opengames, closedGames) = it
            (opengames + closedGames).map { it.players.map { player -> setOfNames.add(player.name) } }
            viewState.postValue(AddPlayersViewState.InitialState(setOfNames.toList()))
        }
    }

    fun launch(args: AddPlayersFragmentArgs) {
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
                    repository.updateGame(game)
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
                repository.updateGame(game)
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