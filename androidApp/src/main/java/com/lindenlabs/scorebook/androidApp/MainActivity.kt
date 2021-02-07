package com.lindenlabs.scorebook.androidApp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewState

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { viewBinding() }

    private val viewModel: HomeViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(HomeViewModel::class.java)

    private fun viewBinding(): ActivityMainBinding {
        val rootView = findViewById<View>(R.id.main_view)
        return ActivityMainBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            binding.updateUi()
            viewModel.viewState.observe(this, ::processViewState)
        }
    }

    private fun ActivityMainBinding.updateUi() {
        val homeScreenBinding = IncludeHomeScreenBinding.bind(findViewById<View>(R.id.homeScrenRoot))
        homeScreenBinding.gameRuleSwitch.textOff = getString(R.string.high_score)
        homeScreenBinding.gameRuleSwitch.textOn = getString(R.string.low_score)
        toolbar.setTitle(R.string.app_name)
    }

    private fun processViewState(viewState: HomeViewState) = when (viewState) {
        HomeViewState.InitialState -> {
        }
    }
}


