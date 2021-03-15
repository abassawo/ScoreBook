package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.databinding.HeaderItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.entities.GameInteraction
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity.BodyType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity.HeaderType
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.BodyViewHolder
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameViewHolder.HeaderViewHolder
import com.lindenlabs.scorebook.androidApp.views.rv.ItemTouchHelperAdapter
import com.lindenlabs.scorebook.androidApp.views.rv.SwipableViewHolder

internal class GameAdapter(diffHelper: DiffUtil.ItemCallback<GameRowEntity> = GameDiffUtilCallback()) :
    ListAdapter<GameRowEntity, GameViewHolder>(diffHelper),
    ItemTouchHelperAdapter {
    val data: MutableList<GameRowEntity> = mutableListOf()
    private var headersCount: Int = 0

    companion object {
        const val HEADER_VIEW_TYPE = 11
        const val BODY_VIEW_TYPE = 22
    }

    class GameDiffUtilCallback : DiffUtil.ItemCallback<GameRowEntity>() {

        override fun areItemsTheSame(oldItem: GameRowEntity, newItem: GameRowEntity): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: GameRowEntity, newItem: GameRowEntity): Boolean =
            oldItem == newItem
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
        var setOfHeaders: Set<GameRowEntity.HeaderType> = mutableSetOf()

        with(this.data) {
            clear()
            entities.forEach { entity ->
                add(entity)
                    .also {
                        if (entity is HeaderType) setOfHeaders += entity
                    }
            }

            headersCount = setOfHeaders.size
        }
        notifyDataSetChanged()
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

sealed class GameViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    internal class HeaderViewHolder(private val binding: HeaderItemRowBinding) :
        GameViewHolder(binding) {

        fun bind(row: HeaderType) = with(binding.text1) { text = row.title }
    }

    internal class BodyViewHolder(private val binding: GameItemRowBinding) :
        GameViewHolder(binding), SwipableViewHolder {

        private lateinit var row: BodyType

        fun bind(row: BodyType) {
            this.row = row
            with(binding.gameName) { text = row.game.name }
            itemView.setOnClickListener {
                row.clickAction(GameInteraction.GameClicked(row.game))
            }
        }

        override fun onItemSwiped(position: Int) {
            row.swipeAction(GameInteraction.SwipeToDelete(row.game))
        }
    }
}
