package com.lindenlabs.scorebook.androidApp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameAdapter

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { viewBinding() }
    private val gameBinding: IncludeHomeScreenBinding by lazy { homeScreenBinding()}
    private val gameAdapter = GameAdapter()

    private fun homeScreenBinding() =
        IncludeHomeScreenBinding.bind(findViewById<View>(R.id.homeScrenRoot))

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
            viewModel.viewState.observe(this, ::showGames)
            viewModel.viewEvent.observe(this, ::showTextError)
        }
    }

    private fun showTextError(event: HomeViewEvent) = when(event){
        HomeViewEvent.AlertNoTextEntered -> homeScreenBinding().enterNewGameEditText.setError("Enter a name for this game!",
            getDrawable(android.R.drawable.stat_notify_error))
    }

    private fun ActivityMainBinding.updateUi() {
        toolbar.setTitle(R.string.app_name)
        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }

        fun IncludeHomeScreenBinding.bind() {
            gameRuleSwitch.textOff = getString(R.string.high_score)
            gameRuleSwitch.textOn = getString(R.string.low_score)
            newGameButton.setOnClickListener {
                val enteredText =  enterNewGameEditText.text.toString()
                viewModel.handleInteraction(NewGameClicked(enteredText))
            }
        }
        gameBinding.bind()
    }

    private fun showGames(viewState: HomeViewState) =
        gameAdapter.setData(viewState.entities)
}


