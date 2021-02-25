package com.lindenlabs.scorebook.androidApp.screens.addplayers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.BaseActivity
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersActivityBinding
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.addplayers.AddPlayersViewState.*
import java.util.*

class AddPlayersActivity : BaseActivity() {
    private val binding: AddPlayersActivityBinding by lazy { viewBinding() }

    private fun viewBinding(): AddPlayersActivityBinding {
        val rootView = findViewById<View>(R.id.addPlayersRoot)
        return AddPlayersActivityBinding.bind(rootView)
    }
    private val viewModel: AddPlayersViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(AddPlayersViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_players_activity)
        viewModel.run {
            viewState.observe(this@AddPlayersActivity, ::processViewState)
            viewEvent.observe(this@AddPlayersActivity, ::processViewEvent)
            launch(appNavigator)
        }
        binding.updateUI()
    }

    private fun processViewState(viewState: AddPlayersViewState) = when(viewState) {
        is TextEntryError -> binding.enterNewPlayerEditText.setError("Enter a valid name")
        is UpdateCurrentPlayersText -> {
            binding.doneButton.visibility = View.VISIBLE
            binding.playersText.text = viewState.playersText
            binding.enterNewPlayerEditText.setText("")
        }
        is ValidateTextForPlusButton -> {
            binding.addAnotherPlayer.run {
                isEnabled = viewState.isEnabled
                visibility = if (isEnabled) View.VISIBLE else View.GONE
            }
        }
        TypingState -> binding.doneButton.visibility = View.GONE
    }

    private fun processViewEvent(viewEvent: AddPlayersViewEvent) {
        Log.d("APA", "Viewevent processed")
        when(viewEvent) {
            is AddPlayersViewEvent.NavigateToGameDetail -> {
                val bundle = AppNavigator.AppBundle.GameDetailBundle(viewEvent.game)
                appNavigator.navigate(this, Destination.GameDetail(bundle))
            }
        }
    }

    private fun AddPlayersActivityBinding.updateUI() {
        this.doneButton.setOnClickListener {
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
        fun newIntent(context: Context, game: Game) : Intent {
            return Intent(context, AddPlayersActivity::class.java)
                .putExtra(GAME_ID_KEY, game.id)
        }
    }
}

