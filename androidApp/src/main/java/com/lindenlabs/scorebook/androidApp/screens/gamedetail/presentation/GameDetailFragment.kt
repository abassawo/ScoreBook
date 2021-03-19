package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import android.os.Bundle
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
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerDataEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragmentDirections.Companion.navigateToAddPlayersScreen
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragmentDirections.Companion.navigateToUpdatePoints
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.PlayerAdapter
import javax.inject.Inject

open class GameDetailFragment : Fragment(R.layout.game_detail_fragment) {
    private val adapter: PlayerAdapter = PlayerAdapter()
    private val binding: GameDetailFragmentBinding by lazy { viewBinding() }
    val viewModel: GameViewModel by lazy {
        viewModelFactory.makeViewModel(this, GameViewModel::class.java)
    }
    private val args: GameDetailFragmentArgs by navArgs()
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
                    viewModel.handleInteraction(GameDetailInteraction.GoBack)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.endGameMenuItem -> {
                viewModel.handleInteraction(GameDetailInteraction.EndGameClicked)
                true
            }
            R.id.restartGameMenuItem -> {
                viewModel.handleInteraction(GameDetailInteraction.RestartGameClicked)
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            viewState.observe(viewLifecycleOwner, ::showGameState)
            viewEvent.observe(viewLifecycleOwner, ::processViewEvent)
        }
        binding.updateUi()

    }

    private fun GameDetailFragmentBinding.updateUi() {
        this.addNewPlayerButton.setOnClickListener { viewModel.navigateToAddPlayerPage() }
        this.gameParticipantsRv.adapter = adapter
        this.gameParticipantsRv.addItemDecoration(DividerItemDecoration(requireContext(), 1))
        this.toolbar.title = "Games"

    }

    private fun processViewEvent(event: GameDetailEvent) {
        when (event) {
            is ActiveGame.GoBackHome -> navigateHome()
            is ActiveGame.AddPlayersClicked -> navigateToAddPlayers(event.game)
            is ActiveGame.EditScoreForPlayer -> navigateToUpdatePlayerScore(event)
            is ActiveGame.EndGame -> endGame(event.game)
            is ClosedGame.RestartGame -> Unit //todo
        }
    }


    private fun navigateHome() = navController.navigate(GameDetailFragmentDirections.navigateHome())

    private fun navigateToUpdatePlayerScore(event: ActiveGame.EditScoreForPlayer) =
        navController.navigate(navigateToUpdatePoints(event.game, event.player))

    private fun navigateToAddPlayers(game: Game) =
        navController.navigate(navigateToAddPlayersScreen(game))

    private fun showGameState(state: GameDetailViewState) {
        binding.toolbar.title = state.gameName

        when (state) {
            is GameDetailViewState.NotStarted -> {
                binding.showEmptyState()
            }
            is GameDetailViewState.StartedWithPlayers -> {
                binding.toolbar.inflateMenu(R.menu.active_game_menu)
                binding.showGame(state.playerData)
            }
            is GameDetailViewState.ClosedGame -> {
                binding.toolbar.inflateMenu(R.menu.closed_game_menu)
                binding.showGame(state.playerData)
            }
        }
    }

    private fun endGame(game: Game) {
        val directions = GameDetailFragmentDirections.navigateToVictoryScreen(game)
        findNavController().navigate(directions)
    }

    private fun GameDetailFragmentBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailFragmentBinding.showGame(players: List<PlayerDataEntity>) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(players)
    }
}
