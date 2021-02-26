package com.lindenlabs.scorebook.androidApp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.base.BaseActivity
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameAdapter

class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy { viewBinding() }
    private val gameBinding: IncludeHomeScreenBinding by lazy { homeScreenBinding()}
    private val gameAdapter = GameAdapter()

    private fun homeScreenBinding() =
        IncludeHomeScreenBinding.bind(findViewById(R.id.homeScrenRoot))

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
            viewModel.viewEvent.observe(this, ::processViewEvent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun processViewEvent(event: HomeViewEvent) = when(event){
        is AlertNoTextEntered -> showError(event)
        is ShowGameDetail -> showGameDetail(event.game)
    }

    private fun showGameDetail(game: Game) {
        val bundle = AppNavigator.AppBundle.GameDetailBundle(game)
        appNavigator.navigate(this, Destination.GameDetail(bundle))
    }

    private fun showError(event: HomeViewEvent.AlertNoTextEntered) {
        val errorPair = event.errorText to  getDrawable(android.R.drawable.stat_notify_error)
        gameBinding.enterNewGameEditText.setError(errorPair.first, errorPair.second)
    }

    private fun ActivityMainBinding.updateUi() {
        toolbar.setTitle(R.string.app_name)
        gamesRecyclerView.adapter = gameAdapter

        fun IncludeHomeScreenBinding.bind() =
            with(gameRuleSwitch) {
                textOff = getString(R.string.high_score)
                textOn = getString(R.string.low_score)
                newGameButton.setOnClickListener {
                    val enteredText = enterNewGameEditText.text.toString()
                    viewModel.handleInteraction(GameDetailsEntered(enteredText, isChecked))
                    enterNewGameEditText.setText("")
                }
            }

        gameBinding.bind()
    }

    private fun showGames(viewState: HomeViewState) =
        gameAdapter.setData(viewState.entities)
}
