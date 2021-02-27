package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.SharedViewModel
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailFragmentBinding
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator.AppBundle
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.showplayers.PlayerAdapter
import com.lindenlabs.scorebook.androidApp.sharedViewModel

class GameDetailFragment : Fragment(R.layout.game_detail_fragment) {
    private val adapter: PlayerAdapter = PlayerAdapter()
    private lateinit var binding: GameDetailFragmentBinding

    private fun View.viewBinding(): GameDetailFragmentBinding {
        val rootView = findViewById<View>(R.id.game_detail_root)
        return GameDetailFragmentBinding.bind(rootView)
    }

    private val viewModel: GameViewModel by lazy { viewModel() }
    private val sharedViewModel: SharedViewModel by lazy { sharedViewModel(this) }

    private fun viewModel() = ViewModelProvider(this).get(GameViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewState.observe(this, ::showGameState)
        viewModel.viewEvent.observe(this, ::processViewEvent)
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
            is ScoreBookViewEvent.EditScoreForPlayer -> navigateToAddScoreForPlayer(event)
            ScoreBookViewEvent.GoBackHome -> Unit  // navigateHome()
        }
    }

//    override fun onBackPressed() = navigateHome()

//    private fun navigateHome() = appNavigator.navigate(this, Destination.HomeScreen)

    private fun navigateToAddScoreForPlayer(event: ScoreBookViewEvent.EditScoreForPlayer) {
        val bundle = AppBundle.UpdatePointsBundle(event.game, event.player)
//        appNavigator.navigate(this, Destination.UpdatePoints(bundle))
    }

    private fun navigateToAddPlayers(game: Game) {
        val bundle = AppBundle.AddPlayersBundle(game)
//        appNavigator.navigate(this, Destination.AddPlayers(bundle))
    }

    private fun showGameState(state: ScoreBookViewState) {
        binding.toolbar.title = state.gameName
        when (state) {
            is ScoreBookViewState.EmptyState -> binding.showEmptyState()
            is ScoreBookViewState.ActiveGame -> binding.showActiveGame(state)
            is ScoreBookViewState.GameOver -> processOutcome(state.result)
        }
    }

    private fun processOutcome(outcome: String) {
//       Toast.makeText(this, outcome, Toast.LENGTH_LONG).show()
    }

    private fun GameDetailFragmentBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailFragmentBinding.showActiveGame(state: ScoreBookViewState.ActiveGame) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(state.scoreBooks)
    }
}
