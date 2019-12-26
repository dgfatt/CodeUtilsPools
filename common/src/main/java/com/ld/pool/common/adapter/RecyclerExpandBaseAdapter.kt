package com.ld.pool.common.adapter

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.data.entity.ExpandGroupIndexEntity
import com.ld.pool.data.entity.ExpandGroupItemEntity


abstract class RecyclerExpandBaseAdapter<G, C, VH : RecyclerView.ViewHolder?> @JvmOverloads constructor(
    dataList: List<ExpandGroupItemEntity<G, C>>? = null
) :
    PinnedHeaderAdapter<VH>() {
    var mDataList: List<ExpandGroupItemEntity<G, C>>?
    var mIndexMap: SparseArray<ExpandGroupIndexEntity>

    var data: List<Any>?
        get() = mDataList
        set(dataList) {
            mDataList = dataList as List<ExpandGroupItemEntity<G, C>>?
            mIndexMap.clear()
            notifyDataSetChanged()
        }

    override fun isPinnedPosition(position: Int): Boolean {
        return getItemViewType(position) == VIEW_TYPE_ITEM_TIME
    }

    override fun getItemViewType(position: Int): Int {
        var count = 0
        for (item in mDataList!!) {
            count += 1
            if (position == count - 1) {
                return VIEW_TYPE_ITEM_TIME
            }
            if (item.childList != null && item.isExpand) {
                count += item.childList!!.size
            }
            if (position < count) {
                return VIEW_TYPE_ITEM_CONTENT
            }
        }
        throw IllegalArgumentException("getItemViewType exception")
    }

    override fun getItemCount(): Int {
        if (mDataList == null || mDataList!!.isEmpty()) {
            return 0
        }
        var count = 0
        for (group in mDataList!!.indices) {
            val item: ExpandGroupItemEntity<G, C> = mDataList!![group]
            //标题
            count += 1
            mIndexMap.put(
                count - 1,
                ExpandGroupIndexEntity(
                    group,
                    -1,
                    if (item.childList == null) 0 else item.childList!!.size
                )
            )
            val childStartPosition = count
            if (item.childList != null && item.isExpand) { //sub
                count += item.childList!!.size
            }
            val childEndPosition = count
            for (loop in childStartPosition until childEndPosition) {
                mIndexMap.put(
                    loop, ExpandGroupIndexEntity(
                        group, loop - childStartPosition,
                        if (item.childList == null) 0 else item.childList!!.size
                    )
                )
            }
        }
        return count
    }

    companion object {
        const val VIEW_TYPE_ITEM_TIME = 0
        const val VIEW_TYPE_ITEM_CONTENT = 1
    }

    init {
        mDataList = dataList
        mIndexMap = SparseArray<ExpandGroupIndexEntity>()
    }
}