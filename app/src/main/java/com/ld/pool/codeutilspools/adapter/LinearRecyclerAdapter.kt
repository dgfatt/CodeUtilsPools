package com.ld.pool.codeutilspools.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.adapter.PinnedHeaderAdapter


class LinearRecyclerAdapter @JvmOverloads constructor(private var mDataList: List<String>? = null) :
    PinnedHeaderAdapter<RecyclerView.ViewHolder?>() {
    fun setData(dataList: List<String>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM_TIME) {
            TitleHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_linear_title,
                    parent,
                    false
                )
            )
        } else {
            ContentHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_linear_content,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM_TIME) {
            val titleHolder = holder as TitleHolder
            titleHolder.mTextTitle.text = mDataList!![position]
        } else {
            val contentHolder = holder as ContentHolder
            contentHolder.mTextTitle.text = mDataList!![position]
        }
    }

    override fun getItemCount(): Int {
        return if (mDataList == null) 0 else mDataList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 5 == 0) {
            VIEW_TYPE_ITEM_TIME
        } else {
            VIEW_TYPE_ITEM_CONTENT
        }
    }

    override fun isPinnedPosition(position: Int): Boolean {
        return getItemViewType(position) == VIEW_TYPE_ITEM_TIME
    }

    internal class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextTitle: TextView = itemView.findViewById(R.id.text_adapter_content_name)

    }

    internal class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextTitle: TextView = itemView.findViewById(R.id.text_adapter_title_name)

    }

    companion object {
        private const val VIEW_TYPE_ITEM_TIME = 0
        private const val VIEW_TYPE_ITEM_CONTENT = 1
    }

}