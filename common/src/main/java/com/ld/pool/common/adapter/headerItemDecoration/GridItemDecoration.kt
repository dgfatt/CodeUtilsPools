package com.ld.pool.common.adapter.headerItemDecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridItemDecoration : ItemDecoration {
    private var dividerDrawable: Drawable?
    private var orientation = LinearLayoutManager.VERTICAL

    constructor(divider: Drawable?) {
        dividerDrawable = divider
    }

    constructor(context: Context, resId: Int) {
        dividerDrawable = context.resources.getDrawable(resId)
    }

    constructor(context: Context, resId: Int, orientation: Int) {
        dividerDrawable = context.resources.getDrawable(resId)
        this.orientation = orientation
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (dividerDrawable == null) {
            return
        }
        if (parent.getChildLayoutPosition(view) < 1) {
            return
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerDrawable!!.intrinsicHeight
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = dividerDrawable!!.intrinsicWidth
        }
    }

    /**
     * @param c
     * @param parent
     * @param state
     */
    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (dividerDrawable == null) {
            return
        }
        val childCount = parent.childCount
        val rightV = parent.width
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val leftV = parent.paddingLeft + child.paddingLeft
            val bottomV = child.top - params.topMargin
            val topV = bottomV - dividerDrawable!!.intrinsicHeight
            val topH = child.top + params.topMargin
            val bottomH = child.bottom + params.bottomMargin
            val rightH = child.left - params.leftMargin
            val leftH = rightH - dividerDrawable!!.intrinsicWidth
            dividerDrawable!!.setBounds(leftH, topH, rightH, bottomH)
            dividerDrawable!!.draw(c)
            dividerDrawable!!.setBounds(leftV, topV, rightV, bottomV)
            dividerDrawable!!.draw(c)
        }
    }
}