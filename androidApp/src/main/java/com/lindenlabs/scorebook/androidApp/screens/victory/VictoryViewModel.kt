package com.lindenlabs.scorebook.androidApp.screens.victory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import com.lindenlabs.scorebook.androidApp.base.domain.PersistentGameRepository
import com.lindenlabs.scorebook.androidApp.di.AppRepository

data class VictoryState(val victoryText: String)

class VictoryViewModel(application: Application) : AndroidViewModel(application) {
    val viewState: MutableLiveData<VictoryState> = MutableLiveData()
    val viewEvent: MutableLiveData<VictoryViewEvent> = MutableLiveData()
    private lateinit var gameRepo: GameDataSource

    fun init(appRepo: AppRepository, args: VictoryFragmentArgs) {
        this.gameRepo = appRepo.gameDataSource
        val game = args.gameArg
        val result = game.end()
        viewState.postValue(VictoryState(result))
        gameRepo.updateGame(game)
    }
}