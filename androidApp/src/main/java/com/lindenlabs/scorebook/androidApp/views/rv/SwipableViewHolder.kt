package com.lindenlabs.scorebook.androidApp.views.rv

import com.lindenlabs.scorebook.shared.common.viewmodel.home.GameRowEntity

interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dismissed by a swipe.
     */
    fun onItemDismiss(position: Int)

    fun restoreItem(item: GameRowEntity, position: Int)
}

interface SwipableViewHolder {
    fun onItemSwiped(position: Int)
}