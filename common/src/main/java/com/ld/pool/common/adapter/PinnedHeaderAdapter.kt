package com.ld.pool.common.adapter

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView


abstract class PinnedHeaderAdapter<VH : RecyclerView.ViewHolder?> :
    RecyclerView.Adapter<VH>() {
    /**
     * 判断该position对应的位置是要固定
     *
     * @param position adapter position
     * @return true or false
     */
    abstract fun isPinnedPosition(position: Int): Boolean

    open fun onCreatePinnedViewHolder(parent: ViewGroup?, viewType: Int): VH {
        return onCreateViewHolder(parent!!, viewType)
    }

    open fun onBindPinnedViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, position)
    }
}