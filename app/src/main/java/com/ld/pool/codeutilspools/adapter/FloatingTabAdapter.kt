package com.ld.pool.codeutilspools.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.ld.pool.codeutilspools.adapter.provider.BannerItemProvider
import com.ld.pool.codeutilspools.adapter.provider.ContentItemProvider
import com.ld.pool.codeutilspools.adapter.provider.FloatingTabItemProvider
import com.ld.pool.data.bean.FloatingTabMultipleBean

/**
 * 构造函数若有传参可以在调用finishInitialize()之前进行赋值，赋值给全局变量
 * 这样getViewType()和registerItemProvider()方法中可以获取到传过来的值
 * getViewType()中可能因为某些业务逻辑，需要将某个值传递过来进行判断，返回对应的viewType
 * registerItemProvider()中可以将值传递给ItemProvider
 */
class FloatingTabAdapter(data: MutableList<FloatingTabMultipleBean>?) :
    MultipleItemRvAdapter<FloatingTabMultipleBean, BaseViewHolder>(data) {

    init {
        //调用该方法告知MultipleItemRvAdapter1️已初始化完构造函数参数的传递
        finishInitialize()
    }

    /**
     * 注册相关的条目provider
     */
    override fun registerItemProvider() {
        //注册Banner条目的itemProvider
        mProviderDelegate.registerProvider(BannerItemProvider());
        //注册Tab条目的itemProvider
        mProviderDelegate.registerProvider(FloatingTabItemProvider());
        //注册Content条目的itemProvider
        mProviderDelegate.registerProvider(ContentItemProvider());
        //注册Empty条目的itemProvider
//        mProviderDelegate.registerProvider(EmptyItemProvider());
    }

    override fun getViewType(item: FloatingTabMultipleBean): Int {
        return item.itemType
    }

}