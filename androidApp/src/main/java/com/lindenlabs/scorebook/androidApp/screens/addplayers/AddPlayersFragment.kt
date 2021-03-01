package com.lindenlabs.scorebook.androidApp.screens.addplayers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersFragmentBinding
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.addplayers.AddPlayersViewState.*
import java.util.*

class AddPlayersFragment : Fragment(R.layout.add_players_fragment) {
    private val binding: AddPlayersFragmentBinding by lazy { viewBinding() }
    val args: AddPlayersFragmentArgs by navArgs()

    private fun viewBinding(): AddPlayersFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPlayersRoot)
        return AddPlayersFragmentBinding.bind(rootView)
    }
    private val viewModel: AddPlayersViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(AddPlayersViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            viewState.observe(viewLifecycleOwner, ::processViewState)
            viewEvent.observe(viewLifecycleOwner, ::processViewEvent)
            viewModel.launch(args)
        }
        binding.updateUI()
    }

    private fun processViewState(viewState: AddPlayersViewState) = when(viewState) {
        is TextEntryError -> binding.enterNewPlayerEditText.setError("Enter a valid name")
        is UpdateCurrentPlayersText -> {
            binding.updatePointsButton.visibility = View.VISIBLE
            binding.playersText.text = viewState.playersText
            binding.enterNewPlayerEditText.setText("")
        }
        is ValidateTextForPlusButton -> {
            binding.addAnotherPlayer.run {
                isEnabled = viewState.isEnabled
                visibility = if (isEnabled) View.VISIBLE else View.GONE
            }
        }
        TypingState -> binding.updatePointsButton.visibility = View.GONE
    }

    private fun processViewEvent(viewEvent: AddPlayersViewEvent) {
        Log.d("APA", "Viewevent processed")
        when(viewEvent) {
            is AddPlayersViewEvent.NavigateToGameDetail -> {
                val directions = AddPlayersFragmentDirections.navigateToScoreGameScreen(viewEvent.game)
                findNavController().navigate(directions)
            }
        }
    }

    private fun AddPlayersFragmentBinding.updateUI() {
        this.updatePointsButton.setOnClickListener {
            val name = binding.enterNewPlayerEditText.text.toString()
            viewModel.handleInteraction(SavePlayerDataAndExit(name)) // new player routes back to Game Detail Screen
        }
        this.addAnotherPlayer.setOnClickListener { // keeps screen on same screen
            val name = binding.enterNewPlayerEditText.text.toString()
            viewModel.handleInteraction(AddAnotherPlayer(name))
        }

        this.enterNewPlayerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { viewModel.handleInteraction(Typing) }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { viewModel.handleInteraction(if(it.isEmpty()) EmptyText else TextEntered)  }

            }
        })
    }

    companion object {
        private const val GAME_ID_KEY = "gameIdKey"

        fun newIntent(arg: AddPlayersFragmentDirections) = Unit
    }
}

