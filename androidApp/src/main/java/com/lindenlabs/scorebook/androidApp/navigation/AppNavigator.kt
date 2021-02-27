package com.lindenlabs.scorebook.androidApp.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lindenlabs.scorebook.androidApp.Destination
import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator.AppBundle.*
import com.lindenlabs.scorebook.androidApp.screens.addplayers.AddPlayersActivity
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsActivity

class AppNavigator {
    var appBundle: AppBundle? = null

    sealed class AppBundle {
        object NoDataBundle : AppBundle()
        data class GameDetailBundle(val game: Game) : AppBundle()
        data class AddPlayersBundle(val game: Game) : AppBundle()
        data class UpdatePointsBundle(val game: Game, val player: Player) : AppBundle()
    }

    fun navigate(activity: Activity, destination: Destination) {
        val intent = destination.toIntent(activity)
        bind(destination)
        activity.startActivity(intent)
        activity.finish()

    }

    private fun bind(destination: Destination) {
        appBundle = when(destination) {
            is Destination.GameDetail -> destination.gameBundle
            is Destination.AddPlayers -> destination.addPlayersBundle
            is Destination.UpdatePoints -> destination.updatePointsBundle
            else -> NoDataBundle
        }
    }


    private fun Destination.toIntent(context: Context): Intent =
        when (this) {
            is Destination.HomeScreen -> Intent(context, MainActivity::class.java)
            is Destination.GameDetail -> Intent(context, MainActivity::class.java) //todo - remove
            is Destination.AddPlayers -> Intent(context, AddPlayersActivity::class.java)
            is Destination.UpdatePoints -> Intent(context, UpdatePointsActivity::class.java)
        }
}