package com.lindenlabs.scorebook.androidApp.views.rv

<<<<<<< HEAD
import com.lindenlabs.scorebook.shared.common.viewmodels.home.GameRowEntity
=======
import com.lindenlabs.scorebook.shared.common.entities.home.GameRowEntity
>>>>>>> Use passed in constructor values for viewmodel


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