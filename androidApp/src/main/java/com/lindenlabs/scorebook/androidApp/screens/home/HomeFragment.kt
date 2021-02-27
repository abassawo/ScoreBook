package com.lindenlabs.scorebook.androidApp.screens.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.lindenlabs.scorebook.androidApp.Destination
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.SharedViewModel
import com.lindenlabs.scorebook.androidApp.databinding.HomeFragmentBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameAdapter
import com.lindenlabs.scorebook.androidApp.sharedViewModel

class HomeFragment : Fragment(R.layout.home_fragment) {
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var  binding: HomeFragmentBinding
    private lateinit var gameBinding: IncludeHomeScreenBinding
    private val gameAdapter = GameAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = view.viewBinding()
        gameBinding = view.homeScreenBinding()
        binding.updateUi()
        viewModel.viewState.observe(this as LifecycleOwner, this::showGames)
        viewModel.viewEvent.observe(this as LifecycleOwner, this::processViewEvent)
    }



    private fun View.homeScreenBinding() =
        IncludeHomeScreenBinding.bind(findViewById(R.id.homeScrenRoot))

    private fun View.viewBinding(): HomeFragmentBinding {
        val rootView = findViewById<View>(R.id.main_view)
        return HomeFragmentBinding.bind(rootView)
    }


    private fun processViewEvent(event: HomeViewEvent) = when(event){
        is HomeViewEvent.AlertNoTextEntered -> showError(event)
        is HomeViewEvent.ShowGameDetail -> showGameDetail(event.game)
    }

    private fun showGameDetail(game: Game) {
        val bundle = AppNavigator.AppBundle.GameDetailBundle(game)
        sharedViewModel = sharedViewModel(this)
        sharedViewModel.processEvent(Destination.GameDetail(gameBundle = bundle))
//        appNavigator.navigate(this, Destination.GameDetail(bundle))
    }

    private fun showError(event: HomeViewEvent.AlertNoTextEntered) {
        val errorPair = event.errorText to  requireContext().getDrawable(android.R.drawable.stat_notify_error)
        gameBinding.enterNewGameEditText.setError(errorPair.first, errorPair.second)
    }

    private fun HomeFragmentBinding.updateUi() {
//        toolbar.setTitle(R.string.app_name)
        gamesRecyclerView.adapter = gameAdapter

        fun IncludeHomeScreenBinding.bind() =
            with(gameRuleSwitch) {
                textOff = getString(R.string.high_score)
                textOn = getString(R.string.low_score)
                newGameButton.setOnClickListener {
                    val enteredText = enterNewGameEditText.text.toString()
                    viewModel.handleInteraction(GameInteraction.GameDetailsEntered(enteredText, isChecked))
                    enterNewGameEditText.setText("")
                }
            }

        gameBinding.bind()
    }

    private fun showGames(viewState: HomeViewState) =
        gameAdapter.setData(viewState.entities)

}

