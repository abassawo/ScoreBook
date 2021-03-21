package com.lindenlabs.scorebook.androidApp.screens.editgame

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.data.raw.GameStrategy
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.databinding.EditGameFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.EditGameModule
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameInteraction
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameViewState
import javax.inject.Inject

class EditGameFragment : Fragment(R.layout.edit_game_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val args: EditGameFragmentArgs by navArgs()
    private val viewModel: EditGameViewModel by lazy {
        viewModelFactory.makeViewModel(this, EditGameViewModel::class.java)
    }
    private val binding: EditGameFragmentBinding by lazy { viewBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        appComponent().value
            .editGameComponentBuilder()
            .plus(EditGameModule(args))
            .build()
            .inject(this)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(EditGameFragmentDirections.navigateBackToGame(args.gameArg))
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun viewBinding(): EditGameFragmentBinding {
        val view: View = requireView().findViewById(R.id.editGameRoot)
        return EditGameFragmentBinding.bind(view)
    }

    private fun handleViewState(viewState: EditGameViewState) {
        when (viewState) {
            is EditGameViewState.Initial -> {
                binding.editGameName.setText(viewState.game.name)
                binding.gameRuleSwitchView.isChecked = viewState.game.strategy == GameStrategy.LowestScoreWins
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
            EditGameViewEvent.Exit -> requireActivity().onBackPressed()
        }
    }
}