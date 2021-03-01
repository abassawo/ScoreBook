package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailFragmentBinding
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentDirections.Companion.navigateToAddPlayersScreen
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.GameDetailFragmentDirections.Companion.navigateToUpdatePoints
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.showplayers.PlayerAdapter

class GameDetailFragment : Fragment(R.layout.game_detail_fragment) {
    private val adapter: PlayerAdapter = PlayerAdapter()
    private val binding: GameDetailFragmentBinding by lazy { viewBinding() }
    private val args: GameDetailFragmentArgs by navArgs()
    private val navController: NavController by lazy { findNavController() }

    private fun viewBinding(): GameDetailFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.game_detail_root)
        return GameDetailFragmentBinding.bind(rootView)
    }

    private val viewModel: GameViewModel by lazy { viewModel() }
//    private val sharedViewModel: SharedViewModel by lazy { sharedViewModel(this) }

    private fun viewModel() = ViewModelProvider(this).get(GameViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            viewState.observe(viewLifecycleOwner, ::showGameState)
            viewEvent.observe(viewLifecycleOwner, ::processViewEvent)
            launch(args)
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
            is ScoreBookViewEvent.AddPlayersClicked -> navigateToAddPlayers(event.game)
            is ScoreBookViewEvent.EditScoreForPlayer -> navigateToUpdatePlayerScore(event)
            ScoreBookViewEvent.GoBackHome -> navigateHome()
        }
    }

    private fun navigateHome() = navController.navigate(GameDetailFragmentDirections.navigateHome())

    private fun navigateToUpdatePlayerScore(event: ScoreBookViewEvent.EditScoreForPlayer) =
        navController.navigate(navigateToUpdatePoints(event.game, event.player))

    private fun navigateToAddPlayers(game: Game) = navController.navigate(navigateToAddPlayersScreen(game))

    private fun showGameState(state: ScoreBookViewState) {
        binding.toolbar.title = state.gameName
        when (state) {
            is ScoreBookViewState.EmptyState -> binding.showEmptyState()
            is ScoreBookViewState.ActiveGame -> binding.showActiveGame(state)
            is ScoreBookViewState.GameOver -> processOutcome(state.result)
        }
    }

    private fun processOutcome(outcome: String) {
       Toast.makeText(requireContext(), outcome, Toast.LENGTH_LONG).show()
    }

    private fun GameDetailFragmentBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailFragmentBinding.showActiveGame(state: ScoreBookViewState.ActiveGame) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(state.scoreBooks)
    }
}
