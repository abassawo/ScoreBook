package com.lindenlabs.scorebook.androidApp.base.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.playerentry.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameViewModel
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewModel
import com.lindenlabs.scorebook.androidApp.screens.victory.VictoryFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.victory.VictoryViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val appRepository: AppRepository, val navArgs: NavArgs) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(appRepository) as T
            GameViewModel::class.java -> GameViewModel(navArgs as GameDetailFragmentArgs) as T
            AddPlayersViewModel::class.java -> AddPlayersViewModel(appRepository, navArgs as AddPlayersFragmentArgs) as T
            UpdatePointsViewModel::class.java -> UpdatePointsViewModel(appRepository, navArgs as UpdatePointsFragmentArgs) as T
            VictoryViewModel::class.java -> VictoryViewModel(appRepository, navArgs as VictoryFragmentArgs) as T
            else -> throw Exception()
        }

    fun <T : ViewModel> makeViewModel(fragment: Fragment, clazz: Class<T>): T =
        ViewModelProvider(fragment, this).get(clazz)
}
