package com.lindenlabs.scorebook.androidApp.screens.home.presentation

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.base.utils.navigate
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.androidApp.databinding.HomeFragmentBinding
import com.lindenlabs.scorebook.androidApp.databinding.IncludeHomeScreenBinding
import com.lindenlabs.scorebook.androidApp.di.ArgModule
import com.lindenlabs.scorebook.androidApp.di.ArgumentPayload
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.rv.GameAdapter
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.welcome.WelcomeDialogFragment
import com.lindenlabs.scorebook.androidApp.views.rv.SwipeToDismissCallback
import com.lindenlabs.scorebook.shared.common.entities.home.HomeInteraction
import com.lindenlabs.scorebook.shared.common.entities.home.HomeViewEvent
import com.lindenlabs.scorebook.shared.common.entities.home.HomeViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import nl.dionsegijn.konfetti.emitters.StreamEmitter
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.home_fragment) {
    private val viewModel: HomeViewModel by lazy {
        viewModelFactory.makeViewModel(this, HomeViewModel::class.java)
    }
    private lateinit var binding: HomeFragmentBinding
    private lateinit var gameBinding: IncludeHomeScreenBinding
    private val gameAdapter = GameAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().value
            .componentBuilder()
            .plus(ArgModule(ArgumentPayload.None))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = view.viewBinding()
        gameBinding = view.homeScreenBinding()
        binding.updateUi()
        viewModel.viewState.observe(this as LifecycleOwner, this::showGames)
        viewModel.viewEvent.observe(this as LifecycleOwner, this::processViewEvent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleInteraction(HomeInteraction.Refresh)
    }

    private fun View.homeScreenBinding() =
        IncludeHomeScreenBinding.bind(findViewById(R.id.homeScrenRoot))

    private fun View.viewBinding(): HomeFragmentBinding {
        val rootView = findViewById<View>(R.id.main_view)
        return HomeFragmentBinding.bind(rootView)
    }

    private fun processViewEvent(event: Event<HomeViewEvent>) {
        when (val action = event.getContentIfNotHandled()) {
            is HomeViewEvent.AlertNoTextEntered -> showError(action)
            is HomeViewEvent.ShowAddPlayersScreen -> showAddPlayersScreen(action.game)
                .also { hideKeyboard() }
            is HomeViewEvent.ShowGameDetail -> navigate(Destination.GameDetail(action.game))
                .also { hideKeyboard() }
            is HomeViewEvent.ShowUndoDeletePrompt -> showUndoPrompt(action)
            HomeViewEvent.ShowWelcomeScreen -> showWelcomeConfetti()
            HomeViewEvent.DismissWelcomeMessage -> binding.viewKonfetti.stopGracefully()
        }
    }

    private fun showAddPlayersScreen(game: Game) =
        navigate(Destination.AddPlayers(game))

    private fun showUndoPrompt(event: HomeViewEvent.ShowUndoDeletePrompt) =
        Snackbar.make(
            requireView(),
            "You've deleted your game: " + event.game.name,
            Snackbar.LENGTH_SHORT
        )
            .setAction(R.string.undo) {
                viewModel.handleInteraction(
                    HomeInteraction.UndoDelete(
                        event.game,
                        event.restoreIndex
                    )
                )
            }.show()

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showError(event: HomeViewEvent.AlertNoTextEntered) {
        val errorPair = event.errorText to ContextCompat.getDrawable(
            requireContext(),
            android.R.drawable.stat_notify_error
        )
        gameBinding.enterNewGameEditText.setError(errorPair.first, errorPair.second)
    }

    private fun showWelcomeConfetti() {
        binding.viewKonfetti.run {
            build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED, Color.BLUE)
                .setDirection(degrees = 5.5)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(4))
                .setPosition(
                    minX = width / 2f,
                    maxX = width + 0f,
                    minY = -50f,
                    maxY = width + 50f
                )
                .streamFor(particlesPerSecond = 30, emittingTime = StreamEmitter.INDEFINITE)
        }
        val dialog = WelcomeDialogFragment {
            viewModel.handleInteraction(HomeInteraction.DismissWelcome)
        }
        dialog.show(requireFragmentManager(), HomeFragment::class.java.simpleName)
    }

    private fun HomeFragmentBinding.updateUi() {
        gamesRecyclerView.run {
            this.adapter = gameAdapter
            this.itemAnimator = DefaultItemAnimator()

            val undoDeleteIcon =
                ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_delete)
            undoDeleteIcon?.let { icon ->
                val itemTouchHelper = ItemTouchHelper(SwipeToDismissCallback(icon))
                itemTouchHelper.attachToRecyclerView(this)
                LinearSnapHelper().attachToRecyclerView(this)
            }
        }.also {
            gameBinding.bind()
        }
    }

    fun IncludeHomeScreenBinding.bind() {
        newGameButton.setOnClickListener {
            val enteredText = enterNewGameEditText.text.toString()
            viewModel.handleInteraction(
                HomeInteraction.GameDetailsEntered(
                    enteredText,
                    gameRuleSwitchView.isLowestScoreStrategyChecked()
                )
            )
            enterNewGameEditText.setText("")
        }
    }

    private fun showGames(viewState: HomeViewState) =
        gameAdapter.setData(viewState.entities)
}
