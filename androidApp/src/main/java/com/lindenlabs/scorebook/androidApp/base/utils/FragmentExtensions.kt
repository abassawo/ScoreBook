package com.lindenlabs.scorebook.androidApp.base.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.SharedNavigationViewModel
import com.lindenlabs.scorebook.androidApp.di.AppComponent
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow

fun Fragment.appComponent(): Lazy<AppComponent> = lazy {
    (requireActivity().application as ScoreBookApplication).appComponent
}

fun Fragment.appRepository() =
    (requireActivity().application as ScoreBookApplication).environment.appRepository

fun Fragment.navigate(destination: Destination) {
    val sharedViewModel by activityViewModels<SharedNavigationViewModel>()
    sharedViewModel.navigateTo(destination)
}

//fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
//    val result = LiveEvent<T>()
//    result.addSource(this) {
//        result.value = it
//    }
//    return result
//}