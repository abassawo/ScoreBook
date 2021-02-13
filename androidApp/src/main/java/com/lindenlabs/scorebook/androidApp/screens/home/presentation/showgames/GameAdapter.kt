package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.BodyViewHolder
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.BodyViewHolder.Companion.BODY_LAYOUT
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.HeaderViewHolder
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.HeaderViewHolder.Companion.HEADER_LAYOUT

class GameAdapter(private val rows: List<GameRowEntity>) :
    Adapter<GameViewHolder>() {

    companion object {
        const val HEADER_VIEW_TYPE = 11
        const val BODY_VIEW_TYPE = 22
    }

    override fun getItemViewType(position: Int): Int {
        return when(rows[position]) {
            is GameRowEntity.HeaderType -> HEADER_VIEW_TYPE
            is GameRowEntity.BodyType -> BODY_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        when(viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(parent.inflate(HEADER_LAYOUT))
            BODY_VIEW_TYPE -> BodyViewHolder(parent.inflate(BODY_LAYOUT))
            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val entity = rows[position]
        when(holder) {
            is HeaderViewHolder -> if (entity is GameRowEntity.HeaderType ) holder.bind(entity)
            is BodyViewHolder -> if (entity is GameRowEntity.BodyType ) holder.bind(entity)
        }
    }

    override fun getItemCount(): Int = rows.size
}

private fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

abstract sealed class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class HeaderViewHolder(view: View) : GameViewHolder(view) {
        private val binding: GameItemRowBinding by lazy { viewBinding() }
        private fun viewBinding(): GameItemRowBinding = GameItemRowBinding.bind(itemView)

        fun bind(row: GameRowEntity.HeaderType) = with(binding.text1) { text = row.title}

        companion object {
            const val HEADER_LAYOUT = R.layout.game_item_row
        }
    }

    class BodyViewHolder(view: View) : GameViewHolder(view) {
        private val binding: GameItemRowBinding by lazy { viewBinding() }
        private fun viewBinding(): GameItemRowBinding = GameItemRowBinding.bind(itemView)

        fun bind(row: GameRowEntity.BodyType) = with(binding.text1) { text = row.game.name }

        companion object {
            const val BODY_LAYOUT = R.layout.game_item_row
        }
    }
}
