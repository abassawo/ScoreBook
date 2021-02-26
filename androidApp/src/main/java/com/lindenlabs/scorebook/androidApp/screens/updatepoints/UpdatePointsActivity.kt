package com.lindenlabs.scorebook.androidApp.screens.updatepoints

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.BaseActivity
import com.lindenlabs.scorebook.androidApp.databinding.AddPointsActivityBinding
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator.*
import com.lindenlabs.scorebook.androidApp.navigation.AppNavigator.AppBundle.*
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.navigation.Destination.*
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewEvent.*
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.UpdatePointsViewModel.*

class UpdatePointsActivity : BaseActivity() {
    private val binding: AddPointsActivityBinding by lazy { viewBinding() }

    private val viewModel: UpdatePointsViewModel by lazy { viewModel() }

    private fun viewModel() = ViewModelProvider(this).get(UpdatePointsViewModel::class.java)

    private fun viewBinding(): AddPointsActivityBinding {
        val rootView = findViewById<View>(R.id.addPointsRoot)
        return AddPointsActivityBinding.bind(rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_points_activity)
        viewModel.viewState.observe(this, ::processState)
        viewModel.viewEvent.observe(this, ::processEvent)
        viewModel.launch(appNavigator)
        binding.doneButton.setOnClickListener {
            val points = Integer.parseInt(binding.pointsEditText.text.toString())
            viewModel.handleInteraction(AddPointsInteraction.AddScore(points))
        }
    }

    private fun processEvent(viewEvent: UpdatePointsViewEvent?) {
        with(viewEvent) {
            when (this) {
                is ScoreUpdated -> appNavigator.navigate(this@UpdatePointsActivity, game.detailScreen())
            }
        }
    }

    private fun Game.detailScreen(): Destination.GameDetail =
        GameDetail(GameDetailBundle(this))

    private fun processState(viewState: UpdatePointsViewState?) {
        when (viewState) {
            is UpdatePointsViewState.ScreenOpened -> binding.toolbar.title = viewState.player.name
        }
    }
}
