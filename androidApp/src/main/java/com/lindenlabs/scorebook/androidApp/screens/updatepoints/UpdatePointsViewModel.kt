package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.androidApp.base.domain.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.data.raw.Player
import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import com.lindenlabs.scorebook.androidApp.di.AppRepository

class UpdatePointsViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var game: Game
    private lateinit var player: Player
    val viewState: MutableLiveData<UpdatePointsViewState> = MutableLiveData()
    val viewEvent: MutableLiveData<UpdatePointsViewEvent> = MutableLiveData()
    private lateinit var gameRepo: GameDataSource

    fun launch(appRepo: AppRepository, args: UpdatePointsFragmentArgs) {
        this.gameRepo = appRepo.gameDataSource
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