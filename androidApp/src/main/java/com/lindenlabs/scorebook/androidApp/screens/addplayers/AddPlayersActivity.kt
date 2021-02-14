package com.lindenlabs.scorebook.androidApp.screens.addplayers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.AddPlayersActivityBinding
import com.lindenlabs.scorebook.androidApp.screens.addplayers.entities.AddPlayerInteraction.*
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

class AddPlayersActivity : AppCompatActivity() {
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
        viewModel.viewEvent.observe(this, ::processViewEvent)
    }

    private fun processViewEvent(viewEvent: AddPlayersViewEvent) {
        val text = binding.enterNewPlayerEditText.editableText.toString()
        when(viewEvent) {
            is AddPlayersViewEvent.NewPlayerAdded -> viewModel.handleInteraction(NewPlayer(text))
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