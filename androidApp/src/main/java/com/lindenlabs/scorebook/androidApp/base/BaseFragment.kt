package com.lindenlabs.scorebook.androidApp.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.R

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId), BackPressHandler {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        retainInstance = true
        (requireActivity() as MainActivity).setNavigationIcon(R.drawable.ic_arrow_back) {
            handleBackPress()
        }
    }

    override fun handleBackPress() {
        (activity as MainActivity).navigateFirstTabWithClearStack()
        findNavController().navigate(R.id.navHomeFragment)
    }
}

interface BackPressHandler {
    fun handleBackPress()
}