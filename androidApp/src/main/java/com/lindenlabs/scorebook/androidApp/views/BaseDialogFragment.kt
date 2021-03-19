package com.lindenlabs.scorebook.androidApp.views

import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = 800
        params.height = 800
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}