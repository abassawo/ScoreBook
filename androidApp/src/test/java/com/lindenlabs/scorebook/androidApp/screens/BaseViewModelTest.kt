package com.lindenlabs.scorebook.androidApp.screens

import com.lindenlabs.scorebook.androidApp.base.Argument
import com.lindenlabs.scorebook.androidApp.base.BaseViewModel
import org.junit.Before

abstract class BaseViewModelTest<V : BaseViewModel> : BaseTest() {
    val underTest: V = initViewModel()

    abstract fun initViewModel(): V


    open fun args(): Argument = Argument.None

    @Before
    override fun before() {
        super.before()
        underTest.launch(environment, args())
    }

    fun startViewModel(action: (viewModel: V) -> Unit) = underTest.run {
        underTest.launch(environment, args())
        action(underTest)
    }
}