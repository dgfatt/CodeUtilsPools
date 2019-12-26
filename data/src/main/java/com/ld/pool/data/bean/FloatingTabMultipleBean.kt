package com.ld.pool.data.bean

import com.ld.pool.data.listener.MultipleItemEntity

class FloatingTabMultipleBean : MultipleItemEntity {

    companion object {
        @JvmStatic
        val ITEM_TYPE_EMPTY: Int = 0

        @JvmStatic
        val ITEM_TYPE_BANNER: Int = 1

        @JvmStatic
        val ITEM_TYPE_TAB: Int = 2

        @JvmStatic
        val ITEM_TYPE_CONTENT: Int = 3

    }

    private var itemType: Int = 0

    lateinit var bannerListBean: MutableList<BannerBean>

    lateinit var tabListBean: MutableList<TabListBean>

    lateinit var contentListBean: ContentListBean

    override fun setItemType(itemType: Int) {
        this.itemType = itemType
    }

    override fun getItemType(): Int {
        return itemType
    }
}