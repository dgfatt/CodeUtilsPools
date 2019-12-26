package com.ld.pool.codeutilspools.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.adapter.RecyclerExpandBaseAdapter
import com.ld.pool.common.utils.ResourceUtils
import com.ld.pool.data.entity.ExpandGroupItemEntity
import com.ld.pool.data.entity.PatrolItemEntity


class PatrolGroupAdapter :
    RecyclerExpandBaseAdapter<String?, PatrolItemEntity?, RecyclerView.ViewHolder?>() {
    /**
     * 悬浮标题栏被点击的时候，展开收起切换功能
     */
    fun switchExpand(adapterPosition: Int) {
        val groupIndex = mIndexMap[adapterPosition].groupIndex
        val entity: ExpandGroupItemEntity<*, *> = mDataList!![groupIndex]
        entity.isExpand = !entity.isExpand
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM_TIME) {
            val holder = TitleItemHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_expand_order_title,
                    parent,
                    false
                )
            )
            holder.itemView.setOnClickListener { v ->
                val entity =
                    v.tag as ExpandGroupItemEntity<*, *>
                entity.isExpand = !entity.isExpand
                notifyDataSetChanged()
            }
            holder
        } else {
            SubItemHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_expand_order_sub,
                    parent,
                    false
                )
            )
        }
    }

    override fun onCreatePinnedViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val holder =
            super.onCreatePinnedViewHolder(parent, viewType) as TitleItemHolder
        holder.mViewSpace.visibility = View.GONE
        return holder
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM_TIME) {
            val groupIndex = mIndexMap[position].groupIndex
            val itemHolder =
                holder as TitleItemHolder
            itemHolder.itemView.tag = mDataList!![groupIndex]
            itemHolder.mTextTime.text = mDataList!![groupIndex].parent
            itemHolder.mImageExpandFlag.setImageResource(
                if (mDataList!![groupIndex].isExpand) R.mipmap.ic_up_indicate else R.mipmap.ic_down_indicate
            )
            if (mDataList!![groupIndex].isExpand) {
                if (mIndexMap[position].childCount == 0) {
                    itemHolder.mViewSpace.visibility = View.VISIBLE
                } else {
                    itemHolder.mViewSpace.visibility = View.GONE
                }
            } else {
                itemHolder.mViewSpace.visibility = View.VISIBLE
            }
        } else {
            val subHolder =
                holder as SubItemHolder
            val groupIndex = mIndexMap[position].groupIndex
            val childIndex = mIndexMap[position].childIndex
            val subItem: PatrolItemEntity = mDataList!![groupIndex].childList!![childIndex]!!
            subHolder.itemView.tag = subItem
            subHolder.mTextTime.text = subItem.time?.substring(11, 16)
            subHolder.mTextCompanyName.text = subItem.factoryName
            subHolder.mTextUsers.text = subItem.user
            subHolder.mTextState.text = getStateDes(
                subHolder.mTextState.context,
                subItem.state
            )
            subHolder.mTextState.setTextColor(
                getStateColor(
                    subHolder.mTextState.context,
                    subItem.state
                )
            )
            val gradientDrawable =
                subHolder.mImageState.background as GradientDrawable
            gradientDrawable.setStroke(
                SizeUtils.dp2px(2F),
                getStateColor(
                    subHolder.mImageState.context,
                    subItem.state
                )
            )
        }
    }

    override fun onBindPinnedViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val itemHolder =
            holder as TitleItemHolder
        itemHolder.mViewSpace.visibility = View.GONE
    }

    internal class TitleItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mViewSpace: View = itemView.findViewById(R.id.view_space)
        var mTextTime: TextView = itemView.findViewById(R.id.text_time)
        var mImageExpandFlag: ImageView = itemView.findViewById(R.id.image_expand_flag)

    }

    internal class SubItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageState: ImageView = itemView.findViewById(R.id.image_state)
        var mTextTime: TextView = itemView.findViewById(R.id.text_time)
        var mTextCompanyName: TextView = itemView.findViewById(R.id.text_company_name)
        var mTextUsers: TextView = itemView.findViewById(R.id.text_repair_user)
        var mTextState: TextView = itemView.findViewById(R.id.text_repair_flag)

    }

    companion object {
        private fun getStateColor(context: Context, state: Int): Int {
            var color: Int = ResourceUtils.getColor(context, R.color.order_state_waiting_reception)
            when (state) {
                0 -> color = ResourceUtils.getColor(context, R.color.order_state_waiting_reception)
                1 -> color = ResourceUtils.getColor(context, R.color.order_state_distributed)
                2 -> color = ResourceUtils.getColor(context, R.color.order_state_progressing)
                3 -> color = ResourceUtils.getColor(context, R.color.order_state_auditing)
                4 -> color = ResourceUtils.getColor(context, R.color.order_state_finished)
                5 -> color = ResourceUtils.getColor(context, R.color.order_state_not_started)
            }
            return color
        }

        private fun getStateDes(context: Context, state: Int): String {
            var des: String = context.getString(R.string.waiting_reception)
            when (state) {
                0 -> des = context.getString(R.string.waiting_reception)
                1 -> des = context.getString(R.string.distributed)
                2 -> des = context.getString(R.string.progressing)
                3 -> des = context.getString(R.string.auditing)
                4 -> des = context.getString(R.string.finished)
                5 -> des = context.getString(R.string.not_started)
            }
            return des
        }
    }
}