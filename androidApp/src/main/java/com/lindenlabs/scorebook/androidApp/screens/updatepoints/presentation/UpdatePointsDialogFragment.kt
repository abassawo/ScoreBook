package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.UpdatePointsFragmentBinding
import com.lindenlabs.scorebook.androidApp.views.BaseDialogFragment
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.engines.updatepoints.UpdatePointsViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player

class UpdatePointsDialogFragment(val refreshAction: () -> Unit) : BaseDialogFragment() {
    private val binding: UpdatePointsFragmentBinding by lazy { viewBinding() }
    private val viewModel: UpdatePointsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.update_points_fragment, container, false)

    private fun viewBinding(): UpdatePointsFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPointsRoot)
        return UpdatePointsFragmentBinding.bind(rootView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = requireArguments()
        viewModel.launch(
            gameId = args["gameArg"] as String,
            playerId = args["playerArg"] as String
        )
        viewModel.viewState.observe(this as LifecycleOwner, ::processState)
        viewModel.viewEvent.observe(this as LifecycleOwner, ::processEvent)

        binding.pointsEditText.requestFocus()

        binding.addPointsButton.setOnClickListener {
            val text = binding.pointsEditText.text.toString()
            viewModel.handleInteraction(UpdatePointsInteraction.ScoreIncreaseBy(text))
            refreshAction()
        }
        binding.deductPointsButton.setOnClickListener {
            val text = binding.pointsEditText.text.toString()
            viewModel.handleInteraction(UpdatePointsInteraction.ScoreLoweredBy(text))
            refreshAction()
        }

    }

    private fun processEvent(viewEvent: UpdatePointsViewEvent) {
        with(viewEvent) {
            when (this) {
                is UpdatePointsViewEvent.ScoreUpdated -> dismiss()
                is UpdatePointsViewEvent.AlertNoTextEntered -> binding.playerName.setError("Must add point")
                UpdatePointsViewEvent.Nil -> Unit
            }
        }
    }

    private fun processState(viewState: UpdatePointsViewState?) {
        when (viewState) {
            is UpdatePointsViewState.ScreenOpened -> binding.playerName.text = viewState.player.name
            UpdatePointsViewState.Loading -> Unit
        }
    }

    companion object {
        fun newInstance(
            game: Game,
            player: Player,
            refreshAction: () -> Unit
        ): UpdatePointsDialogFragment =
            UpdatePointsDialogFragment(refreshAction)
                .apply {
                    arguments = bundleOf("gameArg" to game.id, "playerArg" to player.id)
                }
    }
}
