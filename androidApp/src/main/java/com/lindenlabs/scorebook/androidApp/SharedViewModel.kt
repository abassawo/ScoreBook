package com.lindenlabs.scorebook.androidApp

import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.AppData
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.data.GameDataSource

class SharedViewModel : ViewModel() {
    private lateinit var gameEngine: GameEngine
    private lateinit var gameDataSource: GameDataSource

    fun launch(appData: AppData) {
        this.gameEngine = appData.gameEngine
        this.gameDataSource = appData.gameDataSource
    }
}

