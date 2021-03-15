package com.lindenlabs.scorebook.androidApp.views.rv

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDismissCallback(private val deleteIcon: Drawable, private val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    private val background = ColorDrawable(Color.RED)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (viewHolder as SwipableViewHolder).onItemSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val iconMargin: Int = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconBottom: Int = iconTop + deleteIcon.intrinsicHeight

        val backgroundCornerOffset = 20

        when {
            dX > 0 -> { // Swiping to the right
                val iconLeft: Int = itemView.left + iconMargin + deleteIcon.intrinsicWidth
                val iconRight = itemView.left + iconMargin
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset,
                    itemView.bottom
                )
                deleteIcon.draw(c)
            }
            dX < 0 -> { // Swiping to the left
                val iconLeft: Int = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
                deleteIcon.draw(c)
            }
            else -> { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
    }
}
