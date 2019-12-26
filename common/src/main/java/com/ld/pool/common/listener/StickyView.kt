package com.ld.pool.common.listener

import android.view.View

/**
 *
 *  获取吸附View相关的信息
 */

interface StickyView {
    /**
     * 是否是吸附view
     * @param view
     * @return
     */
    fun isStickyView(view: View?): Boolean

    /**
     * 得到吸附view的itemType
     * @return
     */
    val stickViewType: Int
}