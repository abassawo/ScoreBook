package com.lindenlabs.scorebook.androidApp

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lindenlabs.scorebook.androidApp.di.AppComponent
import kotlinx.coroutines.CoroutineScope


fun Fragment.appComponent(): Lazy<AppComponent> = lazy {
    (requireActivity().application as ScoreBookApplication).appComponent
}

fun ViewModel.viewModelScope(coroutineScope: CoroutineScope? = null) =
    coroutineScope ?: this.viewModelScope
