package com.lindenlabs.scorebook.androidApp.screens

import androidx.lifecycle.ViewModel
import org.junit.Before

abstract class BaseViewModelTest<V : ViewModel> : BaseTest() {
    val underTest: V = initViewModel()

    abstract fun initViewModel(): V

    fun startViewModel(action: (viewModel: V) -> Unit) = underTest.run {
        action(underTest)
    }
}