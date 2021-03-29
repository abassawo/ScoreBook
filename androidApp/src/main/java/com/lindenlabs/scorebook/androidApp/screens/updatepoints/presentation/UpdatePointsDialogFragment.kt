package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.databinding.UpdatePointsFragmentBinding
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewEvent
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewState
import com.lindenlabs.scorebook.androidApp.views.BaseDialogFragment
import com.lindenlabs.scorebook.shared.raw.Game
import com.lindenlabs.scorebook.shared.raw.Player
import javax.inject.Inject

class UpdatePointsDialogFragment(val refreshAction: () -> Unit) : BaseDialogFragment() {
    private val binding: UpdatePointsFragmentBinding by lazy { viewBinding() }
    private val viewModel: UpdatePointsViewModel by lazy { ViewModelFactory().makeViewModel(this, UpdatePointsViewModel::class.java)  }
//    private val args: UpdatePointsDialogFragmentArgs by navArgs()

//    @Inject
//    lateinit var appRepository: AppRepository

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

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
//        appComponent().value
//            .updatePointsComponentBuilder()
//            .plus(UpdatePointsModule())
//            .build()
//            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                is ScoreUpdated -> dismiss()
                is AlertNoTextEntered -> binding.playerName.setError("Must add point")
            }
        }
    }

    private fun processState(viewState: UpdatePointsViewState?) {
        when (viewState) {
            is UpdatePointsViewState.ScreenOpened -> binding.playerName.text = viewState.player.name
        }
    }

    companion object {
        fun newInstance(game: Game, player: Player, refreshAction: () -> Unit): UpdatePointsDialogFragment {
            return UpdatePointsDialogFragment(refreshAction)
//            val directions = GameDetailFragmentDirections.navigateToUpdatePoints(game, player)
//            return UpdatePointsDialogFragment(refreshAction).apply {
//                arguments = directions.arguments
//            }
        }
    }
}
