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
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.base.utils.appRepository
import com.lindenlabs.scorebook.androidApp.base.utils.navigate
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.GameScoreModule
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers.PlayerAdapter
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsDialogFragment
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewEvent
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewEvent.*
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState.WithGameData
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.GameDetailViewState.WithGameData.*
import com.lindenlabs.scorebook.shared.common.engines.gamedetail.PlayerDataEntity
import com.lindenlabs.scorebook.shared.common.raw.Game
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GameDetailFragment : BaseFragment(R.layout.game_detail_fragment) {
    private val binding: GameDetailFragmentBinding by lazy { viewBinding() }
    private val viewModel: GameViewModel by lazy {
        viewModelFactory.makeViewModel(this, GameViewModel::class.java)
    }
    private val adapter: PlayerAdapter = PlayerAdapter()

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
            .plus(GameScoreModule(arguments?.get("gameArg") as String, appRepository()))
            .build()
            .inject(this)
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

    private fun GameDetailFragmentBinding.updateUi() {
        this.addNewPlayerButton.setOnClickListener { viewModel.handleInteraction(GameDetailInteraction.AddPlayerClicked)}
        this.gameParticipantsRv.adapter = adapter
        this.gameParticipantsRv.addItemDecoration(DividerItemDecoration(requireContext(), 1))
        this.toolbar.title = "Games"
    }

    private fun processViewEvent(event: Event<GameDetailViewEvent>) {
        when (val action = event.getContentIfNotHandled()) {
            is GoBackHome -> navigateHome()
            is AddPlayersClicked -> navigateToAddPlayers(action.game)
            is EditScoreForPlayer -> navigateToUpdatePlayerScore(action)
            is EndGame -> endGame(action.game)
            is ShowRestartingGameMessage -> Toast.makeText(
                requireContext(), "${action.game.name} restarting", Toast.LENGTH_SHORT
            ).show()
            is PromptToRestartGame -> showRestartGamePrompt()
            is ConfirmEndGame -> ConfirmEndGameBottomView {
                viewModel.handleInteraction(GameDetailInteraction.EndGameConfirmed)
            }.show(requireFragmentManager(), GameDetailFragment::class.java.simpleName)
            is NavigateToEditHome -> launchEditGameScreen(action.game)
            is None -> Unit
        }
    }

    private fun launchEditGameScreen(game: Game) =
        navigate(Destination.EditGame(game))

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

    private fun navigateToUpdatePlayerScore(event: EditScoreForPlayer) =
        navigate(Destination.UpdatePoints(event.game, event.player))


    private fun navigateToAddPlayers(game: Game) =
        navigate(Destination.AddPlayers(game))


    private fun showGameState(state: GameDetailViewState) {

        fun showState(state: WithGameData) {
            binding.toolbar.title = state.game.name
            binding.toolbar.subtitle = SimpleDateFormat("MMM dd, yyyy", Locale.US)
                .format(Date(state.game.dateCreated))

            when (state) {
                is NotStarted -> {
                    binding.showEmptyState()
                }
                is StartedWithPlayers -> {
                    requireActivity().invalidateOptionsMenu()
                    binding.toolbar.menu.clear()
                    binding.toolbar.inflateMenu(R.menu.active_game_menu)
                    binding.showScoreBoard(state.playerDataEntities)
                }
                is ClosedGame -> {
                    binding.toolbar.menu.clear()
                    binding.toolbar.inflateMenu(R.menu.closed_game_menu)
                    binding.showScoreBoard(state.playerDataEntities)
                }
            }
        }
        (state as? WithGameData)?.let { showState(it) }
    }

    private fun endGame(game: Game) =
        navigate(Destination.VictoryScreen(game))

    private fun GameDetailFragmentBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailFragmentBinding.showScoreBoard(players: List<PlayerDataEntity>) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(players)
    }
}
