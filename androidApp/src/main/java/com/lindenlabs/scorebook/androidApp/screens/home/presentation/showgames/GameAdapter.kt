package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.databinding.HeaderItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity.BodyType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity.HeaderType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.BodyViewHolder
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.HeaderViewHolder

class GameAdapter() :
    Adapter<GameViewHolder>() {
    val data: MutableList<GameRowEntity> = mutableListOf()

    companion object {
        const val HEADER_VIEW_TYPE = 11
        const val BODY_VIEW_TYPE = 22
        const val HEADER_LAYOUT = R.layout.header_item_row
        const val BODY_LAYOUT = R.layout.game_item_row
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is HeaderType -> HEADER_VIEW_TYPE
            is BodyType -> BODY_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        when (viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(parent.inflate(HEADER_LAYOUT))
            BODY_VIEW_TYPE -> BodyViewHolder(parent.inflate(BODY_LAYOUT))
            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val entity = data[position]
        when (holder) {
            is HeaderViewHolder -> if (entity is HeaderType) holder.bind(entity)
            is BodyViewHolder -> if (entity is BodyType) holder.bind(entity)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(entities: List<GameRowEntity>) {
        with(this.data) {
            clear()
            addAll(entities)
        }
        notifyDataSetChanged()
    }
}

private fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

abstract sealed class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class HeaderViewHolder(view: View) : GameViewHolder(view) {
        private val binding: HeaderItemRowBinding by lazy { viewBinding() }
        private fun viewBinding(): HeaderItemRowBinding = HeaderItemRowBinding.bind(itemView)
        fun bind(row: HeaderType) = with(binding.text1) { text = row.title }
    }

    class BodyViewHolder(view: View) : GameViewHolder(view) {
        private val binding: GameItemRowBinding by lazy { viewBinding() }
        private fun viewBinding(): GameItemRowBinding = GameItemRowBinding.bind(itemView)
        fun bind(row: BodyType) = with(binding.text1) { text = row.game.name }
    }
}
