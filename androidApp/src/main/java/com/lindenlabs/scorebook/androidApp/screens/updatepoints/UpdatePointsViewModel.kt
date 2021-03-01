package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.data.GameDataSource
import com.lindenlabs.scorebook.androidApp.data.GameRepository
import com.lindenlabs.scorebook.androidApp.data.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Round

class UpdatePointsViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()
    private val gameRepo = PersistentGameRepository.getInstance(application)

    fun launch(args: UpdatePointsFragmentArgs) {
        this.game = args.gameArg
        this.player = args.playerArg
    }

    fun handleInteraction(interaction: AddPointsInteraction) {
        when (interaction) {
            is AddPointsInteraction.AddScore -> {
                val score = interaction.point
                player.addToScore(score)
                viewEvent.postValue(UpdatePointsViewEvent.ScoreUpdated(player, game))
                gameRepo.updateGame(game)
            }
            is AddPointsInteraction.UndoLastScore -> Unit
        }
    }

    sealed class AddPointsInteraction {
        data class AddScore(val point: Int) : AddPointsInteraction()

        data class UndoLastScore(val scoreHistory: List<Int>) : AddPointsInteraction()
    }
}