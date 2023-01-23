package com.lindenlabs.scorebook.androidApp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameViewModel
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersViewModel
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryViewModel
import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.domain.UserSettings

class ViewModelFactory constructor(
    private val appRepository: AppRepository,
    private val userSettings: UserSettings,
    private val argumentPayload: ArgumentPayload = ArgumentPayload.None
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val argPayload = when (modelClass) {
            HomeViewModel::class.java -> ArgumentPayload.None
            UpdatePointsViewModel::class.java -> argumentPayload as ArgumentPayload.WithGameIdAndPlayerId
            AddPlayersViewModel::class.java,
            EditGameViewModel::class.java,
            GameViewModel::class.java,
            VictoryViewModel::class.java -> argumentPayload as ArgumentPayload.WithGameId
            else -> throw Exception()
        }

        return when (modelClass) {
            GameViewModel::class.java -> GameViewModel(appRepository, userSettings, (argPayload as ArgumentPayload.WithGameId).gameId) as T
            AddPlayersViewModel::class.java -> AddPlayersViewModel(appRepository, (argPayload as ArgumentPayload.WithGameId).gameId) as T

            UpdatePointsViewModel::class.java -> {
                val argumentPayload = argPayload as ArgumentPayload.WithGameIdAndPlayerId
                UpdatePointsViewModel(
                    appRepository,
                    argumentPayload.gameId,
                    argumentPayload.playerId
                ) as T
            }
            VictoryViewModel::class.java -> {
                val argumentPayload = argumentPayload as ArgumentPayload.WithGameId
                VictoryViewModel(appRepository, argumentPayload.gameId) as T
            }
            EditGameViewModel::class.java -> {
                val argumentPayload = argumentPayload as ArgumentPayload.WithGameId
                EditGameViewModel(appRepository, argumentPayload.gameId) as T
            }
            HomeViewModel::class.java -> HomeViewModel(appRepository, userSettings) as T
            else -> throw Exception()
        }
    }

    fun <T : ViewModel> makeViewModel(fragment: Fragment, clazz: Class<T>): T =
        ViewModelProvider(fragment, this).get(clazz)
}


sealed class ArgumentPayload {

    object None : ArgumentPayload()

    open class WithGameId(open val gameId: String) : ArgumentPayload()

    data class WithGameIdAndPlayerId(override val gameId: String, val playerId: String) :
        WithGameId(gameId)
}

