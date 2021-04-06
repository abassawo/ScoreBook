package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.data.*
import com.lindenlabs.scorebook.shared.common.data.persistence.ScoreBookDatabase

class Environment(database: ScoreBookDatabase) {
    val appRepository =
        AppRepository(
            gameDataSource = GameDataSource(database.gameHistoryQueries),
            playersDataSource = PlayerDataSource(database.playerHistoryQueries),
        )
}