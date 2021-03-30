package com.lindenlabs.scorebook.shared.common.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.lindenlabs.scorebook.shared.common.domain.UserSettings

class UserSettingsStore(context: Context): UserSettings {
    private val preferences: SharedPreferences = getDefaultSharedPreferences(context)

    override fun isFirstRun(): Boolean =
        preferences.getBoolean(IS_FIRST_RUN, true)

    override fun clearFirstRun() =
        preferences.edit().putBoolean(IS_FIRST_RUN, false).apply()

    companion object {
        const val IS_FIRST_RUN = "isFirstRun"
    }
}