package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.*
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.BodyViewHolder.Companion.BODY_LAYOUT
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.HeaderViewHolder.Companion.HEADER_LAYOUT

class GameAdapter(private val openGames: List<Game>, private val closedGames: List<Game>) :
    Adapter<GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        viewType.toViewHolder(parent)

    private fun Int.toViewHolder(parent: ViewGroup): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val headerView = inflater.inflate(HEADER_LAYOUT, parent, false)
        val bodyView = inflater.inflate(BODY_LAYOUT, parent, false)

        return when (this) {
            ROW_OPEN_GAMES_HEADER_TYPE -> HeaderViewHolder(headerView, "Open Games")
            ROW_CLOSED_GAMES_HEADER_TYPE -> HeaderViewHolder(headerView, "Closed Games")
            ROW_OPEN_GAMES_HEADER_TYPE -> BodyViewHolder(bodyView)
            else -> throw IllegalStateException("Only defined constants allowed")
        }
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(openGames[position])
    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ROW_OPEN_GAMES_HEADER_TYPE
            openGames.size -> ROW_CLOSED_GAMES_HEADER_TYPE
            else -> ROW_GAME_DATA_BODY_TYPE
        }

    override fun getItemCount(): Int {
        val openHeaderOffset = if (openGames.isNotEmpty()) 1 else 0
        val closedHeaderOffset = if (openGames.isNotEmpty()) 1 else 0
        return openGames.size + closedGames.size + openHeaderOffset + closedHeaderOffset
    }

    companion object {
        const val ROW_OPEN_GAMES_HEADER_TYPE = 100
        const val ROW_CLOSED_GAMES_HEADER_TYPE = 101
        const val ROW_GAME_DATA_BODY_TYPE = 102
    }
}

abstract sealed class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(game: Game)

    class HeaderViewHolder(view: View, val headerText: String) : GameViewHolder(view) {
        private val binding: GameItemRowBinding by lazy { viewBinding() }
        private fun viewBinding(): GameItemRowBinding = GameItemRowBinding.bind(itemView)

        override fun bind(game: Game) = with(binding.text1) { text = headerText }

        companion object {
            const val HEADER_LAYOUT = R.layout.game_item_row
        }
    }

    class BodyViewHolder(view: View) : GameViewHolder(view) {
        private val binding: GameItemRowBinding by lazy { viewBinding() }
        private fun viewBinding(): GameItemRowBinding = GameItemRowBinding.bind(itemView)

        override fun bind(game: Game) = with(binding.text1) { text = game.name }

        companion object {
            const val BODY_LAYOUT = R.layout.game_item_row
        }
    }
}
