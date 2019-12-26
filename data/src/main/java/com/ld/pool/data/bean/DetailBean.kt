package com.ld.pool.data.bean

import com.ld.pool.data.listener.MultipleItemEntity

class DetailBean : MultipleItemEntity {

    companion object {
        @JvmStatic
        val ITEM_TYPE_TXET: Int = 0

        @JvmStatic
        val ITEM_TYPE_TITLE: Int = 1

    }

    private var itemType: Int = 0

    var isTitle: Boolean = false

    var name: String? = null

    var tag: String? = null

    override fun setItemType(itemType: Int) {
        this.itemType = itemType
    }

    override fun getItemType(): Int {
        return itemType
    }

}