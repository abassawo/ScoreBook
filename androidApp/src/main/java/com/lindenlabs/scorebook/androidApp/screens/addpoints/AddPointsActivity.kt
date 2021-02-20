package com.lindenlabs.scorebook.androidApp.screens.addpoints

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.AddPointsActivityBinding
import com.lindenlabs.scorebook.androidApp.screens.addpoints.AddPointsViewModel.*
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class AddPointsActivity  : AppCompatActivity() {
    private val binding: AddPointsActivityBinding by lazy { viewBinding() }

    private val viewModel: AddPointsViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(AddPointsViewModel::class.java)

    private fun viewBinding(): AddPointsActivityBinding {
        val rootView = findViewById<View>(R.id.addPointsRoot)
        return AddPointsActivityBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_points_activity)
        viewModel.viewState.observe(this, ::processState)
        viewModel.viewEvent.observe(this, ::processEvent)

        binding.addPointsRoot.setOnClickListener {
            val points = Integer.parseInt(binding.pointsEditText.text.toString())
            val player = intent.extras?.get(PLAYER_ID_KEY) as Player
            val game = intent.extras?.get(GAME_ID_KEY)
            viewModel.handleInteraction(AddPointsInteraction.AddScore(points))
        }
    }

    private fun processEvent(addPointsViewEvent: AddPointsViewEvent?) {
        when(addPointsViewEvent) {
            is AddPointsViewEvent.ScoreUpdated -> onBackPressed()
        }
    }

    private fun processState(addPointsViewState: AddPointsViewState?) {
        
    }

    companion object {
        private const val GAME_ID_KEY = "gameIdKey"
        private const val PLAYER_ID_KEY = "playerIdKey"

        fun newIntent(context: Context, game: Game, player: Player) : Intent {
            return Intent(context, AddPointsActivity::class.java)
                .putExtra(GAME_ID_KEY, game.id)
                .putExtra(PLAYER_ID_KEY, player.id)
        }
    }
}