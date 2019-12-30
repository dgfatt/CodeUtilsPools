package com.ld.pool.common.listener

import android.view.View

/**
 * 显示自定义View的Group监听
 */
interface PowerGroupListener : GroupListener {
    fun getGroupView(position: Int): View?
}