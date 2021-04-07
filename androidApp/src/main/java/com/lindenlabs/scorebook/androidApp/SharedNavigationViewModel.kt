package com.lindenlabs.scorebook.androidApp

import androidx.lifecycle.ViewModel
import com.lindenlabs.scorebook.androidApp.base.utils.LiveEvent
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.shared.common.Event

class SharedNavigationViewModel : ViewModel() {
    val destinationEvent = LiveEvent<Event<Destination>>()

    fun navigateTo(destination: Destination) {
        destinationEvent.postValue(Event(destination))
    }
}