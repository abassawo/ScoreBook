package com.lindenlabs.scorebook.androidApp.screens.home.presentation.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.lindenlabs.scorebook.androidApp.databinding.WelcomeFragmentBinding

class WelcomeDialogFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = WelcomeFragmentBinding.inflate(inflater).root

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = 800
        params.height = 800
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}