package com.lindenlabs.scorebook.shared.common.data

import android.content.Context
import com.lindenlabs.scorebook.shared.common.data.persistence.ScoreBookDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    }

    fun createDB(): ScoreBookDatabase = ScoreBookDatabase(AppDatabase(createDriver()))
}
