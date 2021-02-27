package com.lindenlabs.scorebook.androidApp

import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator

sealed class Destination(open val id: Int = 0) {
    data class HomeScreen(override val id: Int = R.id.navHomeFragment) : Destination(id)
    data class GameDetail(override val id: Int = R.id.navGameDetail, val gameBundle: AppNavigator.AppBundle.GameDetailBundle) : Destination()
    data class AddPlayers(override val id: Int = 0, val addPlayersBundle: AppNavigator.AppBundle.AddPlayersBundle) : Destination()
    data class UpdatePoints(val updatePointsBundle: AppNavigator.AppBundle.UpdatePointsBundle) : Destination()
}