package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.base.utils.gameIdArg
import com.lindenlabs.scorebook.androidApp.base.utils.playerIdArg
import com.lindenlabs.scorebook.androidApp.databinding.UpdatePointsFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.ArgModule
import com.lindenlabs.scorebook.androidApp.di.ArgumentPayload.WithGameIdAndPlayerId
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsInteraction
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsViewEvent
import com.lindenlabs.scorebook.shared.common.entities.updatepoints.UpdatePointsViewState
import com.lindenlabs.scorebook.shared.common.raw.Game
import com.lindenlabs.scorebook.shared.common.raw.Player
import javax.inject.Inject

class UpdatePointsDialogFragment(val refreshAction: () -> Unit)  : DialogFragment() {
    private val binding: UpdatePointsFragmentBinding by lazy { viewBinding() }
    private val viewModel: UpdatePointsViewModel by lazy {
        viewModelFactory.makeViewModel(
            this,
            UpdatePointsViewModel::class.java
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.update_points_fragment, container, false)

    private fun viewBinding(): UpdatePointsFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPointsRoot)
        return UpdatePointsFragmentBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().value
            .componentBuilder()
            .plus(ArgModule(WithGameIdAndPlayerId(gameIdArg(), playerIdArg())))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(this as LifecycleOwner, ::processState)
        viewModel.viewEvent.observe(this as LifecycleOwner, ::processEvent)

        binding.run {
            pointsEditText.requestFocus()
            addPointsButton.setOnClickListener {
                val text = pointsEditText.text.toString()
                viewModel.handleInteraction(UpdatePointsInteraction.ScoreIncreaseBy(text))
            }
            deductPointsButton.setOnClickListener {
                val text = binding.pointsEditText.text.toString()
                viewModel.handleInteraction(UpdatePointsInteraction.ScoreLoweredBy(text))
            }
        }
    }

    private fun processEvent(viewEvent: Event<UpdatePointsViewEvent>) {
        with(viewEvent.getContentIfNotHandled()) {
            when (this) {
                is UpdatePointsViewEvent.ScoreUpdated -> dismiss().also{ refreshAction() }
                is UpdatePointsViewEvent.AlertNoTextEntered -> binding.playerName.error = "Must add point"
                UpdatePointsViewEvent.Loading -> Unit
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
        fun newInstance(game: Game, player: Player, refreshAction: () -> Unit): UpdatePointsDialogFragment =
            UpdatePointsDialogFragment(refreshAction)
                .apply {
                    arguments = bundleOf("gameArg" to game.id, "playerArg" to player.id)
                }
    }
}
