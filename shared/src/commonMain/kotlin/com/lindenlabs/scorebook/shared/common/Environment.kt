package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.data.AppRepository
import com.lindenlabs.scorebook.shared.common.data.GameDataSource
import com.lindenlabs.scorebook.shared.common.data.PlayerDataSource
import com.lindenlabs.scorebook.shared.common.data.persistence.ScoreBookDatabase

class Environment(database: ScoreBookDatabase) {
    val appRepository = AppRepository(database)

}
