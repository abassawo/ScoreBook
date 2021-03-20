package com.lindenlabs.scorebook.androidApp.base.utils

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.di.AppComponent

fun Fragment.appComponent(): Lazy<AppComponent> = lazy {
    (requireActivity().application as ScoreBookApplication).appComponent
}
