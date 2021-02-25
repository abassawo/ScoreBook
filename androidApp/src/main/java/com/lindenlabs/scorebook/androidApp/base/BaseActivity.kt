package com.lindenlabs.scorebook.androidApp.base

import androidx.appcompat.app.AppCompatActivity
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import java.lang.IllegalStateException

abstract class BaseActivity : AppCompatActivity() {
    val appNavigator by lazy { (application as ScoreBookApplication).appNavigator }

    override fun onBackPressed() {
        super.onBackPressed()
        when(appNavigator.appBundle) {
            AppNavigator.AppBundle.NoDataBundle -> onBackPressed()
            else -> appNavigator.navigate(this, Destination.HomeScreen)
        }
    }
}