package com.lindenlabs.scorebook.androidApp.base.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import com.lindenlabs.scorebook.androidApp.di.AppComponent

fun Fragment.appComponent(): Lazy<AppComponent> = lazy {
    (requireActivity().application as ScoreBookApplication).appComponent
}

fun Activity.hideSoftInput() {
    val imm: InputMethodManager? = getSystemService()
    val currentFocus = currentFocus
    if (currentFocus != null && imm != null) {
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}
