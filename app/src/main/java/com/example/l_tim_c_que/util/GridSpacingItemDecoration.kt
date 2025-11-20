package com.example.l_tim_c_que.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.ItemDecoration] to add equal spacing between grid items.
 *
 * @property spanCount The number of columns in the grid.
 * @property spacing The spacing size in pixels.
 * @property includeEdge Whether to include spacing at the edges of the grid.
 */
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    /**
     * Retrieves the offsets for the given item.
     *
     * @param outRect The Rect to receive the output offsets.
     * @param view The child view to decorate.
     * @param parent The RecyclerView this ItemDecoration is decorating.
     * @param state The current state of RecyclerView.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
