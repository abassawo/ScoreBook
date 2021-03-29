package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.data.GameDataSource
import com.lindenlabs.scorebook.shared.common.data.PlayerDataSource

object Environment {
    val appRepository = AppRepository(GameDataSource(), PlayerDataSource())
}