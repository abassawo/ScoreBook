package com.lindenlabs.scorebook.androidApp.screens.home

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.HomeFragmentBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.HomeViewState
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameAdapter

class HomeFragment : Fragment(R.layout.home_fragment) {
    private lateinit var viewModel: HomeViewModel
    private lateinit var  binding: HomeFragmentBinding
    private lateinit var gameBinding: IncludeHomeScreenBinding
    private val gameAdapter = GameAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    (requireActivity() as Activity).onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

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
        is HomeViewEvent.ShowAddPlayersScreen -> findNavController().showAddPlayersScreen(event.game)
            .also { hideKeyboard() }
        is HomeViewEvent.ShowActiveGame -> findNavController().showActiveGame(event.game)
            .also { hideKeyboard() }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun NavController.showAddPlayersScreen(game: Game) =
        navigate(HomeFragmentDirections.navigateToAddPlayersScreen(game))

    private fun NavController.showActiveGame(game: Game) =
        navigate(HomeFragmentDirections.navigateToScoreGameScreen(game))

    private fun showError(event: HomeViewEvent.AlertNoTextEntered) {
        val errorPair = event.errorText to ContextCompat.getDrawable(
            requireContext(),
            android.R.drawable.stat_notify_error
        )
        gameBinding.enterNewGameEditText.setError(errorPair.first, errorPair.second)
    }

    private fun HomeFragmentBinding.updateUi() {
        gamesRecyclerView.adapter = gameAdapter
        fun IncludeHomeScreenBinding.bind() =
            with(gameRuleSwitch) {
                textOff = getString(R.string.high_score)
                textOn = getString(R.string.low_score)
                newGameButton.setOnClickListener {
                    val enteredText = enterNewGameEditText.text.toString()
                    viewModel.handleInteraction(
                        GameInteraction.GameDetailsEntered(
                            enteredText,
                            isChecked
                        )
                    )
                    enterNewGameEditText.setText("")
                }
            }

        gameBinding.bind()
    }

    private fun showGames(viewState: HomeViewState) =
        gameAdapter.setData(viewState.entities)

}

