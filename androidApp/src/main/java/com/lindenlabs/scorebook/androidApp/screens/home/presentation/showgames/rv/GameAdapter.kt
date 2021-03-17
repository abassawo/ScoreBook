package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.databinding.HeaderItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity.BodyType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity.HeaderType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.rv.GameViewHolder.BodyViewHolder
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.rv.GameViewHolder.HeaderViewHolder
import com.lindenlabs.scorebook.androidApp.views.rv.ItemTouchHelperAdapter
import com.lindenlabs.scorebook.androidApp.views.rv.SwipableViewHolder

internal class GameAdapter : RecyclerView.Adapter<GameViewHolder>(), ItemTouchHelperAdapter {
    val data: MutableList<GameRowEntity> = mutableListOf()
    private var headersCount: Int = 0

    companion object {
        const val HEADER_VIEW_TYPE = 11
        const val BODY_VIEW_TYPE = 22
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
        val diffHelper = GameDiffUtilCallback(this.data, entities)
        val diffResult = DiffUtil.calculateDiff(diffHelper)
        this.data.clear()
        this.data.addAll(entities)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun restoreItem(item: GameRowEntity, position: Int) {
        val headerAdjustedIndex = headersCount + position
        data.add(headerAdjustedIndex, item)
        notifyItemInserted(position)
    }
}

