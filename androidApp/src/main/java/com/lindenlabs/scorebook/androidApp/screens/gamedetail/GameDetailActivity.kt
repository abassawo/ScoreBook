package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailActivityBinding
import com.lindenlabs.scorebook.androidApp.screens.addplayers.AddPlayersActivity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import java.util.*

class GameDetailActivity : AppCompatActivity() {
    private val binding: GameDetailActivityBinding by lazy { viewBinding() }

    private fun viewBinding(): GameDetailActivityBinding {
        val rootView = findViewById<View>(R.id.game_detail_root)
        return GameDetailActivityBinding.bind(rootView)
    }
    private val viewModel: GameViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(GameViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_detail_activity)
        viewModel.viewState.observe(this, ::showGame)
        viewModel.viewEvent.observe(this, ::processViewEvent)
    }

    private fun processViewEvent(event: GameViewEvent) {
        when(event) {
            is GameViewEvent.AddPlayersClicked -> navigateToAddPlayers(event.game)
        }
    }

    private fun navigateToAddPlayers(game: Game) {
        startActivity(AddPlayersActivity.newIntent(this, game))
    }

    private fun showGame(state: GameViewState) {
        when(state) {
            GameViewState.EmptyState -> showEmptyState()
            is GameViewState.StartGame -> showActiveGame(state)
        }
    }

    private fun showEmptyState() {
        binding.emptyStateTextView.visibility = View.VISIBLE
    }

    private fun showActiveGame(state: GameViewState.StartGame) {
        binding.emptyStateTextView.visibility = View.GONE
    }

    companion object {
        private const val GAME_ID_KEY = "Game ID key"
        fun newIntent(context: Context, gameId: UUID) : Intent {
            return Intent(context, GameDetailActivity::class.java)
                .putExtra(GAME_ID_KEY, gameId)
        }
    }
}