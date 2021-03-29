package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayersViewEvent
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayersViewState
import com.lindenlabs.scorebook.shared.common.engines.addplayers.entities.AddPlayersViewState.*
import javax.inject.Inject

class AddPlayersFragment : DialogFragment() {
    private val binding: AddPlayersFragmentBinding by lazy { viewBinding() }
    private val viewModel: AddPlayersViewModel by lazy { ViewModelFactory().makeViewModel(this, AddPlayersViewModel::class.java) }
//    private val args: AddPlayersFragmentArgs by navArgs()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

//    @Inject
//    lateinit var appRepository: AppRepository

    private fun viewBinding(): AddPlayersFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPlayersRoot)
        return AddPlayersFragmentBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        appComponent().value
//            .addPlayersComponentBuilder()
//            .plus(AddPlayersArgsModule())
//            .build()
//            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_players_fragment, container ,false)

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
                val bundle = bundleOf("gameArg" to viewEvent.game)
//                val directions =
//                    AddPlayersFragmentDirections.navigateToScoreGameScreen(viewEvent.game)
                findNavController().navigate(R.id.navActiveGame, bundle).also { hideKeyboard() }
            }
            is AddPlayersViewEvent.NavigateHome -> {
//                findNavController().navigate(AddPlayersFragmentDirections.navigateBackHome())
//                    .also { hideKeyboard()
                    }
            AddPlayersViewEvent.None -> Unit
        }
    }

    private fun hideKeyboard() {
//        val imm: InputMethodManager =
//            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun AddPlayersFragmentBinding.updateUI() {
        this.addPlayersButton.setOnClickListener {
            val name = binding.enterNewPlayerEditText.text.toString()
            viewModel.handleInteraction(AddPlayerInteraction.SavePlayerDataAndExit(name)) // new player routes back to Game Detail Screen
        }
        this.addAnotherPlayer.setOnClickListener { // keeps screen on same screen
            val name = binding.enterNewPlayerEditText.text.toString()
            viewModel.handleInteraction(AddPlayerInteraction.AddAnotherPlayer(name))
        }

        this.enterNewPlayerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                s?.let { viewModel.handleInteraction(Typing) }
            }

            override fun afterTextChanged(s: Editable?) {
//                s?.let { viewModel.handleInteraction(if (it.isEmpty()) EmptyText else TextEntered) }

            }
        })
    }
}

