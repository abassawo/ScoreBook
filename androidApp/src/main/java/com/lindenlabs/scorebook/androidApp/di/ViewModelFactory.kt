package com.lindenlabs.scorebook.androidApp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryViewModel
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.domain.UserSettings
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val appRepository: AppRepository, private val userSettings: UserSettings) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            GameViewModel::class.java -> GameViewModel(appRepository) as T
            AddPlayersViewModel::class.java -> AddPlayersViewModel(appRepository) as T
            UpdatePointsViewModel::class.java -> UpdatePointsViewModel(appRepository) as T
            VictoryViewModel::class.java -> VictoryViewModel(appRepository) as T
            EditGameViewModel::class.java -> EditGameViewModel(appRepository) as T
            HomeViewModel::class.java -> HomeViewModel(appRepository, userSettings) as T
            else -> throw Exception()
        }

    fun <T : ViewModel> makeViewModel(fragment: Fragment, clazz: Class<T>): T =
        ViewModelProvider(fragment, this).get(clazz)
}
