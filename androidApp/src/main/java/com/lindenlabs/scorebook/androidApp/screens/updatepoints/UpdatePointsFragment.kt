package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.presentation.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.extensions.appComponent
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.databinding.UpdatePointsFragmentBinding
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.di.UpdatePointsModule
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewModel.*
import javax.inject.Inject

class UpdatePointsFragment : Fragment(R.layout.update_points_fragment) {
    private val binding: UpdatePointsFragmentBinding by lazy { viewBinding() }
    private val viewModel: UpdatePointsViewModel by lazy { viewModelFactory.makeViewModel(this, UpdatePointsViewModel::class.java)  }
    private val args: UpdatePointsFragmentArgs by navArgs()

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private fun viewBinding(): UpdatePointsFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPointsRoot)
        return UpdatePointsFragmentBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().value
            .updatePointsComponentBuilder()
            .plus(UpdatePointsModule(args))
            .build()
            .inject(this)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                   findNavController().navigate(R.id.navHomeFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(this as LifecycleOwner, ::processState)
        viewModel.viewEvent.observe(this as LifecycleOwner, ::processEvent)
        binding.updatePointsButton.setOnClickListener {
            val points = Integer.parseInt(binding.pointsEditText.text.toString())
            viewModel.handleInteraction(AddPointsInteraction.AddScore(points))
        }
    }

    private fun processEvent(viewEvent: UpdatePointsViewEvent?) {
        with(viewEvent) {
            when (this) {
                is ScoreUpdated -> findNavController().navigateBackToDetailScreen(this.game)
            }
        }
    }

    private fun NavController.navigateBackToDetailScreen(game: Game) =
        navigate(UpdatePointsFragmentDirections.navigateBackToGameDetail(game))

    private fun processState(viewState: UpdatePointsViewState?) {
        when (viewState) {
            is UpdatePointsViewState.ScreenOpened -> binding.toolbar.title = viewState.player.name
        }
    }
}
