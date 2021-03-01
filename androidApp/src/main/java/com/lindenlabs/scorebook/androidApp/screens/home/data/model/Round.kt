package com.lindenlabs.scorebook.androidApp.screens.home.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Round(val roundNumber: Int = 0, val score: Int) : Parcelable