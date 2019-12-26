package com.ld.pool.common.base

import android.view.View
import com.ld.pool.common.listener.StickyView


class ExampleStickyView : StickyView {

    override fun isStickyView(view: View?): Boolean {
        return view?.tag as Boolean
    }

    override val stickViewType: Int
        get() = 11
}