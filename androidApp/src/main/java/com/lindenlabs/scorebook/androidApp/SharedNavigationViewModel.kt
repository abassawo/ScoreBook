package com.lindenlabs.scorebook.androidApp

import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.utils.LiveEvent
import com.lindenlabs.scorebook.androidApp.navigation.Destination

class SharedNavigationViewModel : ViewModel() {
    val destinationEvent = LiveEvent<Destination>()

    fun navigateTo(destination: Destination) {
        destinationEvent.postValue(destination)
    }
}