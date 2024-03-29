package com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.utils.*
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.ArgModule
import com.lindenlabs.scorebook.androidApp.di.ArgumentPayload
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.shared.common.Event
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayerInteraction
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayersViewEvent
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayersViewState
import com.lindenlabs.scorebook.shared.common.entities.addplayers.AddPlayersViewState.*
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class AddPlayersFragment : Fragment() {
    private val binding: AddPlayersFragmentBinding by lazy { viewBinding() }
    private val viewModel: AddPlayersViewModel by lazy {
        viewModelFactory.makeViewModel(
            this,
            AddPlayersViewModel::class.java
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private fun viewBinding(): AddPlayersFragmentBinding {
        val rootView = requireView().findViewById<View>(R.id.addPlayersRoot)
        return AddPlayersFragmentBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        appComponent().value.component().inject(this)

        appComponent().value
            .componentBuilder()
            .plus(ArgModule(ArgumentPayload.WithGameId(gameIdArg())))
            .build()
            .inject(this)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                 navigate(Destination.Home)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_players_fragment, container, false)

    @OptIn(InternalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            viewState.observe(viewLifecycleOwner, ::processViewState)
            viewEvent.observe(viewLifecycleOwner, ::processViewEvent)
            if(savedInstanceState == null) {
//                launch(gameId = requireArguments()["gameArg"] as String)
            }
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

    private fun processViewEvent(viewEvent: Event<AddPlayersViewEvent>) {
        when (val action = viewEvent.getContentIfNotHandled()) {
            is AddPlayersViewEvent.NavigateToGameDetail -> {
                navigate(Destination.GameDetail(action.game))
                    .also { hideKeyboard() }
            }
            is AddPlayersViewEvent.NavigateHome -> {
                navigate(Destination.Home)
                    .also {
                        hideKeyboard()
                    }
            }
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun AddPlayersFragmentBinding.updateUI() {
        this.addPlayersButton.setOnClickListener {
//            val name = binding.enterNewPlayerEditText.text.toString()
            viewModel.handleInteraction(AddPlayerInteraction.SavePlayerDataAndExit) // new player routes back to Game Detail Screen
        }
        this.addAnotherPlayer.setOnClickListener { // keeps screen on same screen
            val name = binding.enterNewPlayerEditText.text.toString()
            viewModel.handleInteraction(AddPlayerInteraction.AddAnotherPlayer(name))
        }

        this.enterNewPlayerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { viewModel.handleInteraction(AddPlayerInteraction.Typing) }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { viewModel.handleInteraction(if (it.isEmpty()) AddPlayerInteraction.EmptyText else AddPlayerInteraction.TextEntered) }
            }
        })
    }
}

