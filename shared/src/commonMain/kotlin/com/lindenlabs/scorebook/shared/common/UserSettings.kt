package com.lindenlabs.scorebook.shared.common

interface UserSettings {

    fun isFirstRun(): Boolean

    fun clearFirstRun()
}