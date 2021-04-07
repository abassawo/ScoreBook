package com.lindenlabs.scorebook.shared.common.data.persistence

import com.lindenlabs.scorebook.shared.common.data.AppDatabase

class ScoreBookDatabase(private val databaseImpl: AppDatabase) : AppDatabase by databaseImpl