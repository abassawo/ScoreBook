package com.lindenlabs.scorebook.androidApp.screens.scorebookdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.BaseActivity
import com.lindenlabs.scorebook.androidApp.databinding.GameDetailActivityBinding
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator.*
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewEvent
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookViewState
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.entities.ScoreBookInteraction
import com.lindenlabs.scorebook.androidApp.screens.scorebookdetail.showplayers.PlayerAdapter
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import java.util.*

class GameDetailActivity : BaseActivity() {
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
        viewModel.launch(appNavigator)
        viewModel.viewState.observe(this, ::showGameState)
        viewModel.viewEvent.observe(this, ::processViewEvent)
        binding.updateUi()
    }

    override fun onResume() {
        super.onResume()
        viewModel.launch(appNavigator)
    }

    private fun GameDetailActivityBinding.updateUi() {
        this.addNewPlayerButton.setOnClickListener { viewModel.navigateToAddPlayerPage() }
        this.gameParticipantsRv.adapter = adapter
        this.gameParticipantsRv.addItemDecoration(DividerItemDecoration(this@GameDetailActivity, 1))
        this.toolbar.title = "Games"
        this.bottomAppbar.setNavigationOnClickListener {
            viewModel.handleInteraction(ScoreBookInteraction.EndGameClicked)
        }
        this.bottomAppbar.setOnMenuItemClickListener {
            viewModel.handleInteraction(ScoreBookInteraction.EndGameClicked)
            true
        }
    }

    private fun processViewEvent(event: ScoreBookViewEvent) {
        when (event) {
            is ScoreBookViewEvent.AddPlayersClicked -> navigateToAddPlayers(event.game)
            is ScoreBookViewEvent.EditScoreForPlayer -> navigateToAddScoreForPlayer(event)
            ScoreBookViewEvent.GoBackHome -> navigateHome()
        }
    }

    override fun onBackPressed() = navigateHome()

    private fun navigateHome() = appNavigator.navigate(this, Destination.HomeScreen)

    private fun navigateToAddScoreForPlayer(event: ScoreBookViewEvent.EditScoreForPlayer) {
        val bundle = AppBundle.UpdatePointsBundle(event.game, event.player)
        appNavigator.navigate(this, Destination.UpdatePoints(bundle))
    }

    private fun navigateToAddPlayers(game: Game) {
        val bundle = AppBundle.AddPlayersBundle(game)
        appNavigator.navigate(this, Destination.AddPlayers(bundle))
    }

    private fun showGameState(state: ScoreBookViewState) {
        binding.toolbar.title = state.gameName
        when (state) {
            is ScoreBookViewState.EmptyState -> binding.showEmptyState()
            is ScoreBookViewState.ActiveGame -> binding.showActiveGame(state)
            is ScoreBookViewState.GameOver -> processOutcome(state.result)
        }
    }

    private fun processOutcome(outcome: String) {
       Toast.makeText(this, outcome, Toast.LENGTH_LONG).show()
    }

    private fun GameDetailActivityBinding.showEmptyState() =
        emptyStateTextView.run { this.visibility = View.VISIBLE }


    private fun GameDetailActivityBinding.showActiveGame(state: ScoreBookViewState.ActiveGame) {
        emptyStateTextView.visibility = View.GONE
        gameParticipantsRv.visibility = View.VISIBLE
        adapter.setData(state.scoreBooks)
    }
}
