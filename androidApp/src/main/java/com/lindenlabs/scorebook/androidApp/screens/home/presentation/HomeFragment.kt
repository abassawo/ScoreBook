package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.databinding.HomeFragmentBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeViewEvent
import com.lindenlabs.scorebook.androidApp.screens.home.entities.HomeViewState
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameAdapter
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder
import com.lindenlabs.scorebook.androidApp.views.rv.SwipeToDismissCallback
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.home_fragment) {
    private val viewModel: HomeViewModel by lazy {
        viewModelFactory.makeViewModel(
            this,
            HomeViewModel::class.java
        )
    }
    private lateinit var binding: HomeFragmentBinding
    private lateinit var gameBinding: IncludeHomeScreenBinding
    private val gameAdapter = GameAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().value.homeFragmentComponent().inject(this)
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


    private fun processViewEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.AlertNoTextEntered -> showError(event)
            is HomeViewEvent.ShowAddPlayersScreen -> findNavController().showAddPlayersScreen(event.game)
                .also { hideKeyboard() }
            is HomeViewEvent.ShowActiveGame -> findNavController().showActiveGame(event.game)
                .also { hideKeyboard() }
            is HomeViewEvent.ShowUndoDeletePrompt -> showUndoPrompt(event)
        }
    }

    private fun showUndoPrompt(event: HomeViewEvent.ShowUndoDeletePrompt) =
        Snackbar.make(requireView(), "You've deleted " + event.game.name, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) {
                viewModel.handleInteraction(UndoDelete(event.game))
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
        ItemTouchHelper(SwipeToDismissCallback())
            .attachToRecyclerView(gamesRecyclerView)

        gamesRecyclerView.adapter = gameAdapter

        fun IncludeHomeScreenBinding.bind() =
            with(gameRuleSwitch) {
                textOff = getString(R.string.high_score)
                textOn = getString(R.string.low_score)
                newGameButton.setOnClickListener {
                    val enteredText = enterNewGameEditText.text.toString()
                    viewModel.handleInteraction(
                        GameDetailsEntered(
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

