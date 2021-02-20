package com.lindenlabs.scorebook.androidApp.screens.managegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.managegame.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.managegame.entities.AddPlayerInteraction.*
import java.util.*

class AddPlayersViewModel : ViewModel() {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private val repository: GameDataSource = GameRepository
    private var game: Game? = null


    fun launch(gameId: UUID) {
        this.game = repository.getGameById(gameId)
        game?.let {
            val players = repository.getPlayers(it)
            if (players.isNotEmpty()) {
                viewState.postValue(AddPlayersViewState.UpdateCurrentPlayersText(players.toText()))
            }
        }
    }

    fun handleInteraction(interaction: AddPlayerInteraction) {
        when (interaction) {
            is SavePlayerDataAndExit -> {
                val players = repository.getPlayers(game!!)
                if (players.isEmpty()) {
                    viewState.postValue(AddPlayersViewState.TextEntryError)
                } else {
                    viewEvent.postValue(AddPlayersViewEvent.NavigateToGameDetail)
                }
                // navigate to Game Detail screen
            }
            is AddAnotherPlayer -> {
                // stay on same screen
                if (interaction.playerName.isEmpty())
                    viewState.postValue(AddPlayersViewState.TextEntryError)
                else {
                    val player = Player(interaction.playerName,)
                    val players = repository.addPlayers(game!!, listOf(player))
                    val playersText = players.toText()
                    viewState.postValue(AddPlayersViewState.UpdateCurrentPlayersText(playersText))
                }
            }
            is TextEntered -> viewState.postValue(AddPlayersViewState.ValidateTextForPlusButton(true))
            is EmptyText -> viewState.postValue(AddPlayersViewState.ValidateTextForPlusButton(false))
            is Typing -> viewState.postValue(AddPlayersViewState.TypingState)
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