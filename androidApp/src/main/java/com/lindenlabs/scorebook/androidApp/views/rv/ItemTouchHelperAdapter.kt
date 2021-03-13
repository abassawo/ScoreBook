package com.lindenlabs.scorebook.androidApp.views.rv

interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dismissed by a swipe.
     */
    fun onItemDismiss(position: Int)
}

interface SwipableViewHolder {
    fun onItemSwiped(position: Int)
}