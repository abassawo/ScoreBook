package com.lindenlabs.scorebook.androidApp.screens.home.presentation.welcome

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.lindenlabs.scorebook.androidApp.databinding.WelcomeFragmentBinding
import com.lindenlabs.scorebook.androidApp.views.BaseDialogFragment

class WelcomeDialogFragment(val action: () -> Unit) : BaseDialogFragment() {
    private lateinit var binding: WelcomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(inflater)
        binding.exitWelcomeBtn.setOnClickListener {
            action()
            this.dismiss()
        }
        return binding.root
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        action()
    }
}