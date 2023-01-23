package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.data.persistence.ScoreBookDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

//actual class DatabaseFactory {
//    actual fun createDriver(): SqlDriver {
//        return NativeSqliteDriver(AppDatabase.Schema, "app.db")
//    }
//
//    fun createDB(): ScoreBookDatabase = ScoreBookDatabase(AppDatabase(createDriver()))
//}