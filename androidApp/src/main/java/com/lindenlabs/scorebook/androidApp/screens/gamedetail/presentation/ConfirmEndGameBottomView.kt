package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lindenlabs.scorebook.androidApp.R

class ConfirmEndGameBottomView(val confirmAction: () -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
       MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to end this game?")
            .setNegativeButton(R.string.common_no) { _, _ -> }
            .setPositiveButton(R.string.common_yes) { _, _ -> confirmAction()}
            .create()
}