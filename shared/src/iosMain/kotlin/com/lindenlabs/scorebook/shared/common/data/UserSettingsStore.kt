package com.lindenlabs.scorebook.shared.common.data

import com.lindenlabs.scorebook.shared.common.domain.UserSettings

actual class UserSettingsStore : UserSettings {

    override fun isFirstRun(): Boolean = true

    override fun clearFirstRun() {

    }
}