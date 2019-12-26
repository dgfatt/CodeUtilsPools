package com.ld.pool.codeutilspools.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ld.pool.codeutilspools.R
import com.ld.pool.data.bean.PerformerBean

class PerformerListAdapter(mPerformers: MutableList<PerformerBean>?) :
    BaseMultiItemQuickAdapter<PerformerBean, BaseViewHolder>(mPerformers) {

    /**
     * 集合
     */
    private var mPerformers: List<PerformerBean>? = mPerformers

    init {
        addItemType(PerformerBean.ITEM_TYPE_TXET, R.layout.item_common_rv);
        addItemType(PerformerBean.ITEM_TYPE_TITLE, R.layout.item_detail_title);
    }

    override fun convert(helper: BaseViewHolder, item: PerformerBean?) {
        when (item?.itemType) {
            PerformerBean.ITEM_TYPE_TITLE -> {
                helper.itemView.tag = true
                helper.setText(R.id.title_textview, item.name)
            }
            else -> {
                helper.itemView.tag = false
                helper.setText(R.id.textview, item?.name)
            }
        }
    }
}