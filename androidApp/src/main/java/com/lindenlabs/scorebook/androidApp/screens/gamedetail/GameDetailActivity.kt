package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeViewModel
import java.util.*

class GameDetailActivity : AppCompatActivity() {
    private val viewModel: GameViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(GameViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_detail_activity)
        viewModel.viewState.observe(this, ::showGame)
    }

    private fun showGame(state: GameViewState) {
        when(state) {
            GameViewState.EmptyState -> showEmptyState()
            is GameViewState.StartGame -> showActiveGame(state)
        }
    }

    private fun showEmptyState() {

    }

    private fun showActiveGame(state: GameViewState.StartGame) {

    }

    companion object {
        private const val GAME_ID_KEY = "Game ID key"
        fun newIntent(context: Context, gameId: UUID) : Intent {
            return Intent(context, GameDetailActivity::class.java)
                .putExtra(GAME_ID_KEY, gameId)
        }
    }
}