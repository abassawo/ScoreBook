package com.lindenlabs.scorebook.androidApp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.ActiveGameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val appRepository: AppRepository, val navArgs: NavArgs) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(appRepository) as T
            GameViewModel::class.java -> GameViewModel(appRepository, navArgs as ActiveGameDetailFragmentArgs) as T
            AddPlayersViewModel::class.java -> AddPlayersViewModel(appRepository, navArgs as AddPlayersFragmentArgs) as T
            UpdatePointsViewModel::class.java -> UpdatePointsViewModel(appRepository, navArgs as UpdatePointsFragmentArgs) as T
            VictoryViewModel::class.java -> VictoryViewModel(appRepository, navArgs as VictoryFragmentArgs) as T
            else -> throw Exception()
        }

    fun <T : ViewModel> makeViewModel(fragment: Fragment, clazz: Class<T>): T =
        ViewModelProvider(fragment, this).get(clazz)
}
