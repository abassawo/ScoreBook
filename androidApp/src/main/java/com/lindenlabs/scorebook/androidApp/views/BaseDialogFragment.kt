package com.lindenlabs.scorebook.androidApp.views

import android.view.WindowManager
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    override fun onResume() {
        super.onResume()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.show()
        dialog!!.window!!.attributes = lp
    }

}