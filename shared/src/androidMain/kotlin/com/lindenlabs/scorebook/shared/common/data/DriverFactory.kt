package com.lindenlabs.scorebook.shared.common.data

actual class DriverFactory actual constructor(val context: android.content.Context) {

    fun bindContext(context: Context) {
        this.context = context
    }

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, "test.db")
    }
}