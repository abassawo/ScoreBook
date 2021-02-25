package com.lindenlabs.scorebook.androidApp.screens.addplayers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction.*
import java.util.*

class AddPlayersViewModel : ViewModel() {
    val viewState: MutableLiveData<AddPlayersViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<AddPlayersViewEvent> = MutableLiveData()
    private lateinit var repository: GameDataSource
    private lateinit var game: Game


    fun launch(appNavigator: AppNavigator) {
        this.repository = appNavigator.gamesDataSource
        val bundle = (appNavigator.appBundle as AppNavigator.AppBundle.AddPlayersBundle)
        this.game = bundle.game
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
                }
                // navigate to Game Detail screen
            }
            is AddAnotherPlayer -> {
                // stay on same screen
                if (interaction.playerName.isEmpty())
                    viewState.postValue(AddPlayersViewState.TextEntryError)
                else {
                    val player = Player(interaction.playerName)
                    val players = repository.addPlayer(game, player)
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