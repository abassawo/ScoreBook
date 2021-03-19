package com.lindenlabs.scorebook.androidApp.settings

interface UserSettings {

    fun isFirstRun(): Boolean

    fun clearFirstRun()
}