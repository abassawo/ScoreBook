package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.data.GameDataSource
import com.lindenlabs.scorebook.shared.common.data.PlayerDataSource

object AppRepositoryFactory {

    operator fun invoke(): AppRepository {
        val gameDataSource = GameDataSource()
        val playerDataSource = PlayerDataSource()
        return AppRepository(gameDataSource, playerDataSource)
    }
}