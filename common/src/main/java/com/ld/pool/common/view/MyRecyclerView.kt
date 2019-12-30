package com.ld.pool.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.common.base.BaseDecoration


class MyRecyclerView : RecyclerView {

    private var mDecoration: BaseDecoration? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {}

    override fun addItemDecoration(decor: ItemDecoration) {
        if (decor is BaseDecoration) {
            mDecoration = decor
        }
        super.addItemDecoration(decor)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if (mDecoration != null) {
            when (e.action) {
                MotionEvent.ACTION_DOWN -> mDecoration!!.onEventDown(e)
                MotionEvent.ACTION_UP -> if (mDecoration!!.onEventUp(e)) {
                    return true
                }
                else -> {
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }
}