package com.ld.pool.common.utils

import android.view.View
import android.view.ViewGroup
import java.util.*
import kotlin.collections.ArrayList


/**
 * View工具类
 */
object ViewUtil {
    /**
     * 获取带用id的子view
     *
     * @param view
     */
    fun getChildViewWithId(view: View?): MutableList<View> {
        val list: MutableList<View> = ArrayList()
        if (null != view && view is ViewGroup) {
            val viewGroup = view as ViewGroup
            val queue: LinkedList<ViewGroup> = LinkedList()
            queue.add(viewGroup)
            while (!queue.isEmpty()) {
                val current: ViewGroup = queue.removeFirst()
                for (i in 0 until current.childCount) {
                    val childView: View = current.getChildAt(i)
                    if (childView is ViewGroup) {
                        queue.addLast(current.getChildAt(i) as ViewGroup)
                    }
                    if (childView.id !== View.NO_ID) {
                        list.add(childView)
                    }
                }
            }
        }
        return list
    }
}