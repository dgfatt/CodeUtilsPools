package com.ld.pool.common.adapter.dividerItemDecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.data.bean.DetailBean


class GridDividerItemDecoration(context: Context, list: MutableList<DetailBean>) :
    RecyclerView.ItemDecoration() {

    private var mContext: Context? = null
    private var mList: List<DetailBean>? = null

    init {
        mContext = context;
        mList = list;
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.layoutManager is GridLayoutManager) {
            val position = parent.getChildAdapterPosition(view);
            val bean = mList?.get(position)
            if (bean != null && bean.isTitle) {
                outRect.set(0, 0, 0, 0);
            } else if ("0" == bean?.name || "1" == bean?.name) {
                outRect.set(0, 5, 5, 0);
            } else if ("2" == bean?.name || "5" == bean?.name || "8" == bean?.name) {
                outRect.set(0, 5, 0, 0);
            } else if ("9" == bean?.name) {
                outRect.set(0, 5, 5, 5);
            } else {
                outRect.set(0, 5, 5, 0);
            }
        } else if (parent.layoutManager is LinearLayoutManager) {
            outRect.set(0, 0, 0, 5);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }

    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

}