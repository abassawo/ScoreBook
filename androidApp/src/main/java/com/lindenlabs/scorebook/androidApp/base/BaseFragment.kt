package com.lindenlabs.scorebook.androidApp.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.utils.navigate
import com.lindenlabs.scorebook.androidApp.navigation.Destination

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId), BackPressHandler {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        retainInstance = true
    }

    override fun handleBackPress() {
        activity?.onBackPressed()
    }
}

interface BackPressHandler {
    fun handleBackPress()
}