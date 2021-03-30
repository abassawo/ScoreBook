package com.lindenlabs.scorebook.androidApp

import androidx.core.os.bundleOf
import androidx.navigation.NavController

fun NavController.navigate(destination: Int, gameId: String) {
    navigate(destination, bundleOf("gameArg" to gameId))
}