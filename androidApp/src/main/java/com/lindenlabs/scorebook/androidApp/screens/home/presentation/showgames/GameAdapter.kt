package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.databinding.HeaderItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity.BodyType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.GameRowEntity.HeaderType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.BodyViewHolder
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.HeaderViewHolder

internal class GameAdapter() :
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
        with(LayoutInflater.from(parent.context)) {
            when (viewType) {
                HEADER_VIEW_TYPE -> {
                    val binding = HeaderItemRowBinding.inflate(this, parent, false)
                    HeaderViewHolder(binding)
                }
                BODY_VIEW_TYPE -> {
                    val binding = GameItemRowBinding.inflate(this, parent, false)
                    BodyViewHolder(binding)
                }
                else -> throw IllegalStateException()
            }
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

abstract sealed class GameViewHolder(open val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    internal class HeaderViewHolder(override val binding: HeaderItemRowBinding) :
        GameViewHolder(binding) {

        fun bind(row: HeaderType) = with(binding.text1) { text = row.title }
    }

    internal class BodyViewHolder(override val binding: GameItemRowBinding) :
        GameViewHolder(binding) {

        fun bind(row: BodyType) {
            with(binding.text1) { text = row.game.name }
            itemView.setOnClickListener {
                row.clickAction(GameInteraction.GameClicked(row.game))
            }
        }
    }
}
