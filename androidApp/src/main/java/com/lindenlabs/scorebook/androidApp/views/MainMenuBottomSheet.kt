package com.lindenlabs.scorebook.androidApp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lindenlabs.scorebook.androidApp.databinding.MainMenuBottomSheetBinding

class MainMenuBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: MainMenuBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainMenuBottomSheetBinding.inflate(layoutInflater)
        binding.mainSettings.children.forEach { it.setOnClickListener { dismiss() } }
        binding.root.setOnClickListener { this.dismiss() }
        return binding.root
    }
}