package com.ld.pool.codeutilspools.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ld.pool.codeutilspools.R
import com.ld.pool.data.bean.DetailBean


class DetailRecyclerViewAdapter(data: MutableList<DetailBean>?) :
    BaseMultiItemQuickAdapter<DetailBean, BaseViewHolder>(data) {

    init {
        addItemType(DetailBean.ITEM_TYPE_TXET, R.layout.item_common_rv);
        addItemType(DetailBean.ITEM_TYPE_TITLE, R.layout.item_detail_title);
    }

    override fun convert(helper: BaseViewHolder, item: DetailBean?) {
        when (item?.itemType) {
            DetailBean.ITEM_TYPE_TITLE ->
                helper.setText(R.id.title_textview, item.name)
            else ->
                helper.setText(R.id.textview, item?.name)
        }
    }
}