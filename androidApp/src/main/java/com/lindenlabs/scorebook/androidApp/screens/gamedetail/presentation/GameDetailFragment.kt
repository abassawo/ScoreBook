package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.BaseFragment
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerDataEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.PlayerAdapter
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsDialogFragment
import com.lindenlabs.scorebook.shared.common.raw.Game
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GameDetailFragment : BaseFragment(R.layout.game_detail_fragment) {
    private val binding: GameDetailFragmentBinding by lazy { viewBinding() }
    private val viewModel: GameViewModel by lazy {
        viewModelFactory.makeViewModel(this, GameViewModel::class.java)
    }
//    private val args: GameDetailFragmentArgs by navArgs()
    private val adapter: PlayerAdapter = PlayerAdapter()
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

//        appComponent().value
//            .gameScoreComponentBuilder()
//            .plus(GameScoreModule())
//            .build()
//            .inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).setNavigationIcon(R.drawable.ic_arrow_back) {
            handleBackPress()
        }
    }



    private fun addToolbarListener() = binding.toolbar.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.endGameMenuItem -> {
                viewModel.handleInteraction(GameDetailInteraction.EndGameClicked)
                true
            }

            R.id.restartGameMenuItem -> {
                viewModel.handleInteraction(GameDetailInteraction.RestartGameClicked)
                true
            }
            R.id.editGameMenuItem -> {
                viewModel.handleInteraction(GameDetailInteraction.EditGameClicked)
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
        addToolbarListener()
    }

//    override fun handleBackPress() {
//        findNavController().navigate(GameDetailFragmentDirections.navigateHome())
//    }

    private fun GameDetailFragmentBinding.updateUi() {
        this.addNewPlayerButton.setOnClickListener { viewModel.navigateToAddPlayerPage() }
        this.gameParticipantsRv.adapter = adapter
        this.gameParticipantsRv.addItemDecoration(DividerItemDecoration(requireContext(), 1))
        this.toolbar.title = "Games"

    }

    private fun processViewEvent(event: GameDetailViewEvent) =
        when (event) {
            is GoBackHome -> navigateHome()
            is AddPlayersClicked -> navigateToAddPlayers(event.game)
            is EditScoreForPlayer -> navigateToUpdatePlayerScore(event)
            is EndGame -> endGame(event.game)
            is ShowRestartingGameMessage -> Toast.makeText(
                requireContext(), "${event.game.name} restarting", Toast.LENGTH_SHORT
            ).show()
            is PromptToRestartGame -> showRestartGamePrompt()
            ConfirmEndGame -> ConfirmEndGameBottomView {
                viewModel.handleInteraction(GameDetailInteraction.EndGameConfirmed)
            }.show(requireFragmentManager(), GameDetailFragment::class.java.simpleName)
            is NavigateToEditHome -> Unit // launchEditGameScreen(event.game)
        }

//    private fun launchEditGameScreen(game: Game) =
//        navController.navigate(GameDetailFragmentDirections.navigateToEditGameScreen(game))

    private fun showRestartGamePrompt() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Would you like you to restart this game?")
            .setPositiveButton(
                R.string.common_yes
            ) { _, _ ->
                viewModel.handleInteraction(GameDetailInteraction.RestartGameClicked)
            }
            .setNegativeButton(R.string.common_no) { _, _ -> }
            .create()
            .show()
    }


    private fun navigateHome() = (activity as MainActivity).navigateFirstTabWithClearStack()

    private fun navigateToUpdatePlayerScore(event: EditScoreForPlayer) {
        val refreshAction = { viewModel.refreshScore() }
        val updatePointsDialog = with(event) {
            UpdatePointsDialogFragment.newInstance(game, player, refreshAction)
        }
        updatePointsDialog.show(requireFragmentManager(), GameDetailFragment::class.java.name)
    }

    private fun navigateToAddPlayers(game: Game) = Unit
//        navController.navigate(navigateToAddPlayersScreen(game))

    private fun showGameState(state: GameDetailViewState) {
        binding.toolbar.title = state.game.name
        binding.toolbar.subtitle = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            .format(Date(state.game.dateCreated))

        when (state) {
            is GameDetailViewState.NotStarted -> {
                binding.showEmptyState()
            }
            is GameDetailViewState.StartedWithPlayers -> {
                requireActivity().invalidateOptionsMenu()
                binding.toolbar.menu.clear()
                binding.toolbar.inflateMenu(R.menu.active_game_menu)
                binding.showScoreBoard(state.playerDataEntities)
            }
            is GameDetailViewState.ClosedGame -> {
                binding.toolbar.menu.clear()
                binding.toolbar.inflateMenu(R.menu.closed_game_menu)
                binding.showScoreBoard(state.playerDataEntities)
            }
        }
    }

    private fun endGame(game: Game) {
//        val directions = GameDetailFragmentDirections.navigateToVictoryScreen(game)
//        findNavController().navigate(directions)
    }

    private fun GameDetailFragmentBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailFragmentBinding.showScoreBoard(players: List<PlayerDataEntity>) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(players)
    }
}
