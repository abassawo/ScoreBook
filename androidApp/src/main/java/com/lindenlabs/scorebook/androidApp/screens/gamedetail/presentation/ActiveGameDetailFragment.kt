package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailFragmentBinding
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.di.GameScoreModule
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.ActiveGameDetailFragmentDirections.Companion.navigateToAddPlayersScreen
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.ActiveGameDetailFragmentDirections.Companion.navigateToUpdatePoints
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.PlayerAdapter
import javax.inject.Inject

open class ActiveGameDetailFragment : Fragment(R.layout.game_detail_fragment) {
    private val adapter: PlayerAdapter = PlayerAdapter()
    private val binding: GameDetailFragmentBinding by lazy { viewBinding() }
    val viewModel: GameViewModel by lazy {
        viewModelFactory.makeViewModel(this, GameViewModel::class.java)
    }
    private val args: ActiveGameDetailFragmentArgs by navArgs()
    private val navController: NavController by lazy { findNavController() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private fun viewBinding(): GameDetailFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.game_detail_root)
        return GameDetailFragmentBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        appComponent().value
            .gameScoreComponentBuilder()
            .plus(GameScoreModule(args))
            .build()
            .inject(this)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    viewModel.handleInteraction(ScoreBookInteraction.GoBack)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.active_game_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.endGameMenuItem -> {
                viewModel.handleInteraction(ScoreBookInteraction.EndGameClicked)
                true
            }
            else -> false
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            viewState.observe(viewLifecycleOwner, ::showGameState)
            viewEvent.observe(viewLifecycleOwner, {
                if(it !is ClosedGame) {
                    processViewEvent(it as ActiveGame)
                }
            })
        }
        binding.updateUi()

    }

    private fun GameDetailFragmentBinding.updateUi() {
        this.addNewPlayerButton.setOnClickListener { viewModel.navigateToAddPlayerPage() }
        this.gameParticipantsRv.adapter = adapter
        this.gameParticipantsRv.addItemDecoration(DividerItemDecoration(requireContext(), 1))
        this.toolbar.title = "Games"
        this.bottomAppbar.setNavigationOnClickListener {
            viewModel.handleInteraction(ScoreBookInteraction.EndGameClicked)
        }
    }

    private fun processViewEvent(event: ActiveGame) {
        when (event) {
            is ActiveGame.GoBackHome -> navigateHome()
            is ActiveGame.AddPlayersClicked -> navigateToAddPlayers(event.game)
            is ActiveGame.EditScoreForPlayer -> navigateToUpdatePlayerScore(event)
            is ActiveGame.EndGame -> endGame(event.game)
        }
    }

    private fun navigateHome() = navController.navigate(ActiveGameDetailFragmentDirections.navigateHome())

    private fun navigateToUpdatePlayerScore(event: ActiveGame.EditScoreForPlayer) =
        navController.navigate(navigateToUpdatePoints(event.game, event.player))

    private fun navigateToAddPlayers(game: Game) =
        navController.navigate(navigateToAddPlayersScreen(game))

    private fun showGameState(state: ScoreBookViewState) {
        binding.toolbar.title = state.gameName
        when (state) {
            is ScoreBookViewState.EmptyState -> binding.showEmptyState()
            is ScoreBookViewState.ActiveGame -> binding.showActiveGame(state)
        }
    }

    private fun endGame(game: Game) {
        val directions = ActiveGameDetailFragmentDirections.navigateToVictoryScreen(game)
        findNavController().navigate(directions)
    }

    private fun GameDetailFragmentBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailFragmentBinding.showActiveGame(state: ScoreBookViewState.ActiveGame) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(state.scoreBooks)
    }
}
