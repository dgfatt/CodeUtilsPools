package com.ld.pool.codeutilspools.adapter.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.ld.pool.codeutilspools.R
import com.ld.pool.data.bean.FloatingTabMultipleBean

class ContentItemProvider : BaseItemProvider<FloatingTabMultipleBean, BaseViewHolder>() {
    override fun layout(): Int {
        return R.layout.item_content
    }

    override fun viewType(): Int {
        return FloatingTabMultipleBean.ITEM_TYPE_CONTENT
    }

    override fun convert(helper: BaseViewHolder, data: FloatingTabMultipleBean?, position: Int) {

    }
}