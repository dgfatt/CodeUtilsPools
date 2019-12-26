package com.ld.pool.codeutilspools.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.view.StepProgressLayout


class StepProgressAdapter @JvmOverloads constructor(layoutResId: Int, data: MutableList<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: String?) {
        val position = helper.adapterPosition
        val step: StepProgressLayout = helper.getView(R.id.step_view)
        val tvName: TextView = helper.getView(R.id.tv_name)
        val tvTime: TextView = helper.getView(R.id.tv_time)
        tvName.text = item
        // 第一个位置
        if (position == itemCount - 1) {
            step.changeFirstStyle()
            helper.setTypeface(R.id.tv_name, Typeface.DEFAULT_BOLD)
            helper.setTypeface(R.id.tv_time, Typeface.DEFAULT_BOLD)
            tvName.setTextColor(Color.parseColor("#FFDD2828"))
            tvTime.setTextColor(Color.parseColor("#FFDD2828"))
        } else { // 最后一个位置
            if (position == 0) {
                step.changeEndStyle()
            } else {
                step.changeNormalStyle()
            }
            helper.setTypeface(R.id.tv_name, Typeface.DEFAULT)
            helper.setTypeface(R.id.tv_time, Typeface.DEFAULT)
            tvName.setTextColor(Color.parseColor("#FFAAAAAA"))
            tvTime.setTextColor(Color.parseColor("#FFAAAAAA"))
        }
    }
}