package com.lindenlabs.scorebook.androidApp.base.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.di.AppComponent
import kotlinx.coroutines.flow.MutableStateFlow

fun Fragment.appComponent(): Lazy<AppComponent> = lazy {
    (requireActivity().application as ScoreBookApplication).appComponent
}

fun Fragment.appRepository() =
    (requireActivity().application as ScoreBookApplication).environment.appRepository

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}