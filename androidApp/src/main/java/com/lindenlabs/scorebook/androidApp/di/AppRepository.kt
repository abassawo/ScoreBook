package com.lindenlabs.scorebook.androidApp.di

import com.lindenlabs.scorebook.androidApp.base.domain.GameDataSource
import javax.inject.Inject

class AppRepository @Inject constructor(val gameDataSource: GameDataSource) {

    companion object {
        lateinit var instance: AppRepository
    }
}