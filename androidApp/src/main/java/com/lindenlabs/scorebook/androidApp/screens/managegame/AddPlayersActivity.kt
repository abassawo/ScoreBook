package com.lindenlabs.scorebook.androidApp.screens.managegame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersActivityBinding
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.GameDetailActivity
import com.lindenlabs.scorebook.androidApp.screens.managegame.entities.AddPlayerInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

class AddPlayersActivity : AppCompatActivity() {
    private val binding: AddPlayersActivityBinding by lazy { viewBinding() }

    private fun viewBinding(): AddPlayersActivityBinding {
        val rootView = findViewById<View>(R.id.addPlayersRoot)
        return AddPlayersActivityBinding.bind(rootView)
    }
    private val viewModel: AddPlayersViewModel by lazy { viewModel() }
    private val gameId: UUID by lazy { intent.extras?.get(GAME_ID_KEY) as UUID }

    private fun viewModel() = ViewModelProvider(this).get(AddPlayersViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_players_activity)
        viewModel.run {
            viewState.observe(this@AddPlayersActivity, ::processViewState)
            viewEvent.observe(this@AddPlayersActivity, ::processViewEvent)
            launch(gameId)
        }
        binding.updateUI()
    }

    private fun processViewState(viewState: AddPlayersViewState) = when(viewState) {
        is AddPlayersViewState.UpdateCurrentPlayersText -> {
            binding.playersText.text = viewState.playersText
            binding.enterNewPlayerEditText.setText("")
        }
        AddPlayersViewState.TextEntryError -> binding.enterNewPlayerEditText.setError("Enter a valid name")
    }

    private fun processViewEvent(viewEvent: AddPlayersViewEvent) {
        Log.d("APA", "Viewevent processed")
        when(viewEvent) {
            is AddPlayersViewEvent.NavigateToGameDetail -> onBackPressed()
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
    }

    companion object {
        private const val GAME_ID_KEY = "gameIdKey"
        fun newIntent(context: Context, game: Game) : Intent {
            return Intent(context, AddPlayersActivity::class.java)
                .putExtra(GAME_ID_KEY, game.id)
        }
    }
}

