package com.lindenlabs.scorebook.shared.common

import com.lindenlabs.scorebook.shared.common.data.*
import com.lindenlabs.scorebook.shared.common.data.persistence.ScoreBookDatabase
import kotlinx.coroutines.CoroutineScope

class Environment(database: ScoreBookDatabase) {
    val appRepository =
        AppRepository(GameDataSource(database.gameHistoryQueries), PlayerDataSource(database.playerHistoryQueries), )
}