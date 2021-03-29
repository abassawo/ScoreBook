package com.lindenlabs.scorebook.androidApp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameViewModel
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
//            HomeViewModel::class.java -> HomeViewModel() as T
            GameViewModel::class.java -> GameViewModel() as T
            AddPlayersViewModel::class.java -> AddPlayersViewModel() as T
            UpdatePointsViewModel::class.java -> UpdatePointsViewModel() as T
            VictoryViewModel::class.java -> VictoryViewModel() as T
            EditGameViewModel::class.java -> EditGameViewModel() as T
            else -> throw Exception()
        }

    fun <T : ViewModel> makeViewModel(fragment: Fragment, clazz: Class<T>): T =
        ViewModelProvider(fragment, this).get(clazz)
}
