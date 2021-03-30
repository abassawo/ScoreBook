package com.lindenlabs.scorebook.androidApp.screens.editgame

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.BaseFragment
import com.lindenlabs.scorebook.androidApp.databinding.EditGameFragmentBinding
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameInteraction
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameViewEvent
import com.lindenlabs.scorebook.shared.common.engines.editgame.EditGameViewState
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import javax.inject.Inject

class EditGameFragment : BaseFragment(R.layout.edit_game_fragment) {
    private val viewModel: EditGameViewModel by viewModels()
    private val binding: EditGameFragmentBinding by lazy { viewBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun handleBackPress() = viewModel.handleInteraction(EditGameInteraction.Cancel)

    private fun viewBinding(): EditGameFragmentBinding {
        val view: View = requireView().findViewById(R.id.editGameRoot)
        return EditGameFragmentBinding.bind(view)
    }

    private fun handleViewState(viewState: EditGameViewState) {
        when (viewState) {
            is EditGameViewState.Initial -> {
                binding.editGameName.setText(viewState.game.name)
//                binding.gameRuleSwitchView.isChecked = viewState.game.strategy == GameStrategy.LowestScoreWins
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(this as LifecycleOwner, {
            handleViewState(it)
        })
        viewModel.viewEvent.observe(this as LifecycleOwner, {
            handleViewEvent(it)
        })
        with(binding) {
            saveGameButton.setOnClickListener {
                val enteredText = editGameName.text.toString()
                val isChecked = gameRuleSwitchView.isChecked
                val newGameStrategy =
                    if (isChecked) GameStrategy.LowestScoreWins else GameStrategy.HighestScoreWins
                viewModel.handleInteraction(
                    EditGameInteraction.SaveChanges(
                        enteredText,
                        newGameStrategy
                    )
                )
            }

            cancelEditButton.setOnClickListener { viewModel.handleInteraction(EditGameInteraction.Cancel) }
        }
    }

    private fun handleViewEvent(viewEvent: EditGameViewEvent) {
        when (viewEvent) {
            EditGameViewEvent.ShowTextEntryError -> {
                val drawable = ContextCompat.getDrawable(
                    requireContext(),
                    android.R.drawable.stat_notify_error
                )
                drawable?.let { binding.editGameName.setError("", drawable) }

            }
            is EditGameViewEvent.ReturnToGameDetail ->{
//                (requireActivity() as MainActivity).navigateFirstTabWithClearStack()
//                findNavController().navigate(EditGameFragmentDirections.navigateBackToGame(viewEvent.game))
            }
        }
    }
}