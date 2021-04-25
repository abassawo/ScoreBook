package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.rv

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.databinding.HeaderItemRowBinding
import com.lindenlabs.scorebook.androidApp.views.rv.SwipableViewHolder
import com.lindenlabs.scorebook.shared.common.viewmodels.home.GameRowEntity
import com.lindenlabs.scorebook.shared.common.viewmodels.home.HomeInteraction

sealed class GameViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    internal class HeaderViewHolder(private val binding: HeaderItemRowBinding) :
        GameViewHolder(binding) {

        fun bind(row: GameRowEntity.HeaderType) = with(binding.text1) { text = row.title }
    }

    internal class BodyViewHolder(private val binding: GameItemRowBinding) :
        GameViewHolder(binding), SwipableViewHolder {

        private lateinit var row: GameRowEntity.BodyType

        fun bind(row: GameRowEntity.BodyType) {
            this.row = row
            with(binding.gameName) { text = row.game.name }
            itemView.setOnClickListener {
                row.clickAction(HomeInteraction.GameClicked(row.game))
            }
        }

        override fun onItemSwiped(position: Int) {
            row.swipeAction(HomeInteraction.SwipeToDelete(row.game))
        }
    }
}