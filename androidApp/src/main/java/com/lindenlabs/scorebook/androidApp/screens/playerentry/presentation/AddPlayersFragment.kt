package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.AddPlayersArgsModule
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayersViewEvent
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayersViewState
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayersViewState.*
import javax.inject.Inject

class AddPlayersFragment : Fragment(R.layout.add_players_fragment) {
    private val binding: AddPlayersFragmentBinding by lazy { viewBinding() }
    private val viewModel: AddPlayersViewModel by lazy { viewModelFactory.makeViewModel(this, AddPlayersViewModel::class.java) }
    private val args: AddPlayersFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appRepository: AppRepository

    private fun viewBinding(): AddPlayersFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPlayersRoot)
        return AddPlayersFragmentBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(AddPlayersFragmentDirections.navigateBackHome())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        appComponent().value
            .addPlayersComponentBuilder()
            .plus(AddPlayersArgsModule(args))
            .build()
            .inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            viewState.observe(viewLifecycleOwner, ::processViewState)
            viewEvent.observe(viewLifecycleOwner, ::processViewEvent)
        }
        binding.updateUI()
    }

    private fun processViewState(viewState: AddPlayersViewState) = when (viewState) {
        is TextEntryError -> binding.enterNewPlayerEditText.setError("Enter a valid name")
        is UpdateCurrentPlayersText -> {
            binding.addPlayersButton.visibility = View.VISIBLE
            binding.playersText.text = viewState.playersText
            binding.enterNewPlayerEditText.setText("")
        }
        is PlusButtonEnabled -> {
            binding.addAnotherPlayer.run {
                isEnabled = viewState.isEnabled
                visibility = if (isEnabled) View.VISIBLE else View.GONE
            }
        }
        TypingState -> binding.addPlayersButton.visibility = View.GONE
        is LoadAutocompleteAdapter -> {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.suggestedPlayerNames
            );
            binding.enterNewPlayerEditText.setAdapter(adapter)
        }
    }

    private fun processViewEvent(viewEvent: AddPlayersViewEvent) {
        Log.d("APA", "Viewevent processed")
        when (viewEvent) {
            is AddPlayersViewEvent.NavigateToGameDetail -> {
                val directions =
                    AddPlayersFragmentDirections.navigateToScoreGameScreen(viewEvent.game)
                findNavController().navigate(directions).also { hideKeyboard() }
            }
            AddPlayersViewEvent.NavigateHome ->
                findNavController().navigate(AddPlayersFragmentDirections.navigateBackHome())
                    .also { hideKeyboard() }
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun AddPlayersFragmentBinding.updateUI() {
        this.addPlayersButton.setOnClickListener {
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
                s?.let { viewModel.handleInteraction(if (it.isEmpty()) EmptyText else TextEntered) }

            }
        })
    }
}

