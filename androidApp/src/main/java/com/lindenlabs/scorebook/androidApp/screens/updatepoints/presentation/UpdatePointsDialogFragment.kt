package com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.data.raw.Game
import com.lindenlabs.scorebook.androidApp.databinding.UpdatePointsFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.UpdatePointsModule
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsViewModel.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewEvent
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.entities.UpdatePointsViewState
import com.lindenlabs.scorebook.androidApp.views.BaseDialogFragment
import javax.inject.Inject

class UpdatePointsDialogFragment : BaseDialogFragment() {
    private val binding: UpdatePointsFragmentBinding by lazy { viewBinding() }
    private val viewModel: UpdatePointsViewModel by lazy { viewModelFactory.makeViewModel(this, UpdatePointsViewModel::class.java)  }
    private val args: UpdatePointsDialogFragmentArgs by navArgs()

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_points_fragment, container, false)
    }

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
            val text = binding.pointsEditText.text.toString()
            viewModel.handleInteraction(AddPointsInteraction.ScoreAdded(text))
        }
    }

    private fun processEvent(viewEvent: UpdatePointsViewEvent) {
        with(viewEvent) {
            when (this) {
                is ScoreUpdated -> findNavController().navigateBackToDetailScreen(this.game)
                is AlertNoTextEntered -> binding.playerName.setError("Must add point")
            }
        }
    }

    private fun NavController.navigateBackToDetailScreen(game: Game) =
        navigate(UpdatePointsDialogFragmentDirections.navigateBackToGameDetail(game))

    private fun processState(viewState: UpdatePointsViewState?) {
        when (viewState) {
            is UpdatePointsViewState.ScreenOpened -> binding.playerName.text = viewState.player.name
        }
    }
}
