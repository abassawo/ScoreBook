package com.lindenlabs.scorebook.androidApp.screens.victory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.androidApp.base.domain.PersistentGameRepository

data class VictoryState(val victoryText: String)

class VictoryViewModel(application: Application) : AndroidViewModel(application) {
    val viewState: MutableLiveData<VictoryState> = MutableLiveData()
    val viewEvent: MutableLiveData<VictoryViewEvent> = MutableLiveData()
    private val gameRepo = PersistentGameRepository.getInstance(application)

    fun init(args: VictoryFragmentArgs) {
        val game = args.gameArg
        val result = game.end()
        viewState.postValue(VictoryState(result))
        gameRepo.updateGame(game)
    }
}