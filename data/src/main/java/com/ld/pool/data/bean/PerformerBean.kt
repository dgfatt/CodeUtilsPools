package com.ld.pool.data.bean

import com.ld.pool.data.listener.MultipleItemEntity

class PerformerBean constructor(name: String) : MultipleItemEntity {

    /**
     * 名字
     */
    var name: String = ""

    /**
     * item type
     */
    private var itemType: Int = 11

    init {
        this.name = name
    }

    companion object {
        @JvmStatic
        val ITEM_TYPE_TXET: Int = 10

        @JvmStatic
        val ITEM_TYPE_TITLE: Int = 11

    }

    constructor(name: String, itemType: Int) : this(name) {
        this.name = name
        this.itemType = itemType
    }

    override fun setItemType(itemType: Int) {
        this.itemType = itemType
    }

    override fun getItemType(): Int {
        return itemType;
    }
}