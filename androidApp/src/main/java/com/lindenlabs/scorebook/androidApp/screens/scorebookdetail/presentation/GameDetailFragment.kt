package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.presentation

import android.os.Bundle
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
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentArgs
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentDirections
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentDirections.Companion.navigateToAddPlayersScreen
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentDirections.Companion.navigateToUpdatePoints
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.presentation.showplayers.PlayerAdapter
import javax.inject.Inject

class GameDetailFragment : Fragment(R.layout.game_detail_fragment) {
    private val adapter: PlayerAdapter = PlayerAdapter()
    private val binding: GameDetailFragmentBinding by lazy { viewBinding() }
    private val viewModel: GameViewModel by lazy { viewModelFactory.makeViewModel(this, GameViewModel::class.java) }
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
        appComponent().value
            .gameScoreComponentBuilder()
            .plus(GameScoreModule((args)))
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
        this.bottomAppbar.setNavigationOnClickListener {
            viewModel.handleInteraction(ScoreBookInteraction.EndGameClicked)
        }
        this.bottomAppbar.setOnMenuItemClickListener {
            viewModel.handleInteraction(ScoreBookInteraction.EndGameClicked)
            true
        }
    }

    private fun processViewEvent(event: ScoreBookViewEvent) {
        when (event) {
            is GoBackHome -> navigateHome()
            is AddPlayersClicked -> navigateToAddPlayers(event.game)
            is EditScoreForPlayer -> navigateToUpdatePlayerScore(event)
            is EndGame -> endGame(event.game)
        }
    }

    private fun navigateHome() = navController.navigate(GameDetailFragmentDirections.navigateHome())

    private fun navigateToUpdatePlayerScore(event: EditScoreForPlayer) =
        navController.navigate(navigateToUpdatePoints(event.game, event.player))

    private fun navigateToAddPlayers(game: Game) = navController.navigate(navigateToAddPlayersScreen(game))

    private fun showGameState(state: ScoreBookViewState) {
        binding.toolbar.title = state.gameName
        when (state) {
            is ScoreBookViewState.EmptyState -> binding.showEmptyState()
            is ScoreBookViewState.ActiveGame -> binding.showActiveGame(state)
        }
    }

    private fun endGame(game: Game) {
        val directions = GameDetailFragmentDirections.navigateToVictoryScreen(game)
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
