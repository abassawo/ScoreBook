package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.UpdatePointsFragmentBinding
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewModel.*

class UpdatePointsFragment : Fragment(R.layout.update_points_fragment) {
    private val binding: UpdatePointsFragmentBinding by lazy { viewBinding() }

    private val viewModel: UpdatePointsViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(UpdatePointsViewModel::class.java)

    private fun viewBinding(): UpdatePointsFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPointsRoot)
        return UpdatePointsFragmentBinding.bind(rootView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(this as LifecycleOwner, ::processState)
        viewModel.viewEvent.observe(this as LifecycleOwner, ::processEvent)
        binding.doneButton.setOnClickListener {
            val points = Integer.parseInt(binding.pointsEditText.text.toString())
            viewModel.handleInteraction(AddPointsInteraction.AddScore(points))
        }
    }


    private fun processEvent(viewEvent: UpdatePointsViewEvent?) {
//        with(viewEvent) {
//            when (this) {
//                is ScoreUpdated -> appNavigator.navigate(this@UpdatePointsFragment, game.detailScreen())
//            }
//        }
    }

//    private fun Game.detailScreen(): Destination.GameDetail =
//        Destination.GameDetail(0, GameDetailBundle(this))

    private fun processState(viewState: UpdatePointsViewState?) {
        when (viewState) {
            is UpdatePointsViewState.ScreenOpened -> binding.toolbar.title = viewState.player.name
        }
    }
}
