package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailEvent.ClosedGame
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookInteraction

class ClosedGameDetailFragment : ActiveGameDetailFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewEvent.observe(this as LifecycleOwner, {
            if (it is ClosedGame)
                handleViewEvent(it)
        })
    }

    private fun handleViewEvent(event: ClosedGame) =
        when (event) {
            is ClosedGame.RestartGame -> findNavController().navigate(
                ClosedGameDetailFragmentDirections.restartGame(event.game)
            )
            is ClosedGame.GoBackHome -> viewModel.handleInteraction(ScoreBookInteraction.GoBack)
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.closed_game_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.restartGameMenuItem -> {
                viewModel.handleInteraction(ScoreBookInteraction.RestartGameClicked)
                true
            }
            else -> false
        }
    }
}