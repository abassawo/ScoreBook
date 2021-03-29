package com.lindenlabs.scorebook.shared.common.domain

interface UserSettings {

    fun isFirstRun(): Boolean

    fun clearFirstRun()
}