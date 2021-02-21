package com.lindenlabs.scorebook.androidApp.screens.gamedetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailActivityBinding
import com.lindenlabs.scorebook.androidApp.screens.addpoints.AddPointsActivity
import com.lindenlabs.scorebook.androidApp.screens.managegame.AddPlayersActivity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewEvent
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameViewState
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.showplayers.PlayerAdapter
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

class GameDetailActivity : AppCompatActivity() {
    private val adapter: PlayerAdapter = PlayerAdapter()
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
        intent.extras?.get(GAME_ID_KEY)?.let {
            viewModel.launch(it as UUID)
        }
        viewModel.viewState.observe(this, ::showGameState)
        viewModel.viewEvent.observe(this, ::processViewEvent)
        binding.updateUi()
    }

    override fun onResume() {
        super.onResume()
        intent.extras?.get(GAME_ID_KEY)?.let {
            viewModel.launch(it as UUID)
        }
    }

    private fun GameDetailActivityBinding.updateUi() {
        this.addNewPlayerButton.setOnClickListener { viewModel.navigateToAddPlayerPage() }
        this.gameParticipantsRv.adapter = adapter
        this.gameParticipantsRv.addItemDecoration(DividerItemDecoration(this@GameDetailActivity, 1))
        this.toolbar.title = "Games"
        this.bottomAppbar.setNavigationOnClickListener {
            val gameId = intent.extras?.get(GAME_ID_KEY) as UUID
            viewModel.handleInteraction(PlayerInteraction.EndGameClicked(gameId))
        }
        this.bottomAppbar.setOnMenuItemClickListener {
            val gameId = intent.extras?.get(GAME_ID_KEY) as UUID
            viewModel.handleInteraction(PlayerInteraction.EndGameClicked(gameId))
            true
        }
    }

    private fun processViewEvent(event: GameViewEvent) {
        when (event) {
            is GameViewEvent.AddPlayersClicked -> navigateToAddPlayers(event.game)
            is GameViewEvent.EditScoreForPlayer -> navigateToAddScoreForPlayer(event)
            GameViewEvent.GoBackHome -> onBackPressed()
        }
    }

    private fun navigateToAddScoreForPlayer(event: GameViewEvent.EditScoreForPlayer) {
        startActivity(AddPointsActivity.newIntent(this, event.game, event.player))
    }

    private fun navigateToAddPlayers(game: Game) {
        startActivity(AddPlayersActivity.newIntent(this, game))
    }

    private fun showGameState(state: GameViewState) {
        binding.toolbar.title = state.gameName
        when (state) {
            is GameViewState.EmptyState -> binding.showEmptyState()
            is GameViewState.ActiveGame -> binding.showActiveGame(state)
            is GameViewState.GameOver -> processOutcome(state.result)
        }
    }

    private fun processOutcome(outcome: String) {
       Toast.makeText(this, outcome, Toast.LENGTH_LONG).show()
    }

    private fun GameDetailActivityBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailActivityBinding.showActiveGame(state: GameViewState.ActiveGame) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(state.players)
    }

    companion object {
        private const val GAME_ID_KEY = "Game ID key"
        fun newIntent(context: Context, gameId: UUID): Intent {
            return Intent(context, GameDetailActivity::class.java)
                .putExtra(GAME_ID_KEY, gameId)
        }
    }
}
