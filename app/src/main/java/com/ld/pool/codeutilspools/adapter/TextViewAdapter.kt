package com.ld.pool.codeutilspools.adapter

import android.content.res.Resources
import android.view.Gravity
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ld.pool.codeutilspools.R
import com.ld.pool.codeutilspools.R.color
import com.ld.pool.data.bean.PostBean
import com.ld.pool.data.bean.TextViewBean


class TextViewAdapter(layoutId: Int, data: MutableList<TextViewBean>) :
    BaseQuickAdapter<TextViewBean, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder, item: TextViewBean?) {
        helper.setText(R.id.tv, item?.text)
        val textView = helper.getView<TextView>(R.id.tv)
        val resources: Resources = helper.itemView.context.resources
        if (item is PostBean) {
            textView.gravity = Gravity.LEFT
            textView.setBackgroundColor(resources.getColor(color.colorAccent))
        } else {
            textView.gravity = Gravity.RIGHT
            textView.setBackgroundColor(resources.getColor(color.colorPrimary))
        }
    }
}