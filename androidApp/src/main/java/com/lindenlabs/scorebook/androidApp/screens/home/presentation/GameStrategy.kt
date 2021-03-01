package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class GameStrategy : Parcelable {
    @Parcelize
    object HighestScoreWins : GameStrategy()

    @Parcelize
    object LowestScoreWins : GameStrategy()
}