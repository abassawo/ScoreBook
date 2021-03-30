package com.lindenlabs.scorebook.androidApp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameViewModel
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val gameId: String, private val playerId: String? = null) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            GameViewModel::class.java -> GameViewModel(gameId) as T
            AddPlayersViewModel::class.java -> AddPlayersViewModel(gameId) as T
            UpdatePointsViewModel::class.java -> UpdatePointsViewModel(gameId, playerId!!) as T
            VictoryViewModel::class.java -> VictoryViewModel(gameId) as T
            EditGameViewModel::class.java -> EditGameViewModel(gameId) as T
            else -> throw Exception()
        }

    fun <T : ViewModel> makeViewModel(fragment: Fragment, clazz: Class<T>): T =
        ViewModelProvider(fragment, this).get(clazz)
}
