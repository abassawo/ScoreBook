package com.lindenlabs.scorebook.androidApp.base.utils

import androidx.lifecycle.MutableLiveData
import com.lindenlabs.scorebook.shared.common.Event

fun <T> MutableLiveData<Event<T>>.postEvent(t: T) =
    postValue(t.toLiveEvent())

fun <T> T.toLiveEvent(): Event<T> {
    return Event(this)
}