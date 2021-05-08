package com.lindenlabs.scorebook.androidApp.screens.editgame

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.BaseFragment
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.base.utils.gameIdArg
import com.lindenlabs.scorebook.androidApp.base.utils.navigate
import com.lindenlabs.scorebook.androidApp.databinding.EditGameFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.ArgModule
import com.lindenlabs.scorebook.androidApp.di.ArgumentPayload
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.shared.common.entities.editgame.EditGameInteraction
import com.lindenlabs.scorebook.shared.common.entities.editgame.EditGameViewEvent
import com.lindenlabs.scorebook.shared.common.entities.editgame.EditGameViewState
import com.lindenlabs.scorebook.shared.common.raw.GameStrategy
import javax.inject.Inject

class EditGameFragment : BaseFragment(R.layout.edit_game_fragment) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: EditGameViewModel by lazy {
        viewModelFactory.makeViewModel(this, EditGameViewModel::class.java)
    }
    private val binding: EditGameFragmentBinding by lazy { viewBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        appComponent().value
            .componentBuilder()
            .plus(ArgModule(ArgumentPayload.WithGameId(gameIdArg())))
            .build()
            .inject(this)
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
                binding.gameRuleSwitchView.setLowestScoreStrategyGameEnabled((viewState.game.strategy == GameStrategy.LowestScoreWins))
                binding.gameRuleSwitchView. setLowestScoreStrategyGameEnabled(
                    enabled = viewState.game.strategy == GameStrategy.LowestScoreWins
                )
            }
            EditGameViewState.Loading -> Unit
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = requireArguments()["gameArg"] as String
//        viewModel.launch(gameId)
        viewModel.viewState.observe(this as LifecycleOwner, {
            handleViewState(it)
        })
        viewModel.viewEvent.observe(this as LifecycleOwner, {
            handleViewEvent(it)
        })
        with(binding) {
            saveGameButton.setOnClickListener {
                val enteredText = editGameName.text.toString()
                val isChecked = gameRuleSwitchView.isLowestScoreStrategyChecked()
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
            is EditGameViewEvent.ReturnToGameDetail -> {
                navigate(Destination.GameDetail(viewEvent.game))
            }
            EditGameViewEvent.None -> Unit
        }
    }
}