package com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.rv

import androidx.recyclerview.widget.DiffUtil
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.showgames.GameRowEntity

class GameDiffUtilCallback(val oldList: List<GameRowEntity>, val newList: List<GameRowEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}