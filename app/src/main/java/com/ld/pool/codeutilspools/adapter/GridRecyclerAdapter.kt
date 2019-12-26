package com.ld.pool.codeutilspools.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.adapter.PinnedHeaderAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class GridRecyclerAdapter @JvmOverloads constructor(private val mDataList: List<String>? = null) :
    PinnedHeaderAdapter<RecyclerView.ViewHolder?>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM_TIME) {
            TitleHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_grid_title,
                    parent,
                    false
                )
            )
        } else {
            ContentHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_grid_content,
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
            val titleHolder =
                holder as TitleHolder
            titleHolder.mTextTitle.text = mDataList!![position]
        } else {
            val contentHolder = holder as ContentHolder
            Picasso.get().load(mDataList!![position]).into(contentHolder.mImage)
        }
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
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
        var mImage: CircleImageView = itemView.findViewById(R.id.image_icon)

    }

    internal class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextTitle: TextView = itemView.findViewById(R.id.text_adapter_title_name)

    }

    companion object {
        const val VIEW_TYPE_ITEM_TIME = 0
        private const val VIEW_TYPE_ITEM_CONTENT = 1
    }

}