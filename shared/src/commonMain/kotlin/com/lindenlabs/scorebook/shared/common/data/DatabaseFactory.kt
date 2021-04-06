package com.lindenlabs.scorebook.shared.common.data

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseFactory {
    fun createDriver(): SqlDriver
}

