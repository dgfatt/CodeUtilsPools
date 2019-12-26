package com.ld.pool.common.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.common.listener.IPinnedHeaderDecoration


class PinnedHeaderRecyclerView : RecyclerView {
    interface OnPinnedHeaderClickListener {
        fun onPinnedHeaderClick(adapterPosition: Int)
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    private var mPinnedHeaderClickListener: OnPinnedHeaderClickListener? =
        null

    fun setOnPinnedHeaderClickListener(listener: OnPinnedHeaderClickListener?) {
        mPinnedHeaderClickListener = listener
    }

    private var mPinnedHeaderHandle = false
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if (mPinnedHeaderClickListener == null) {
            return super.onInterceptTouchEvent(e)
        }
        val pinnedHeaderInterface: IPinnedHeaderDecoration = pinnedHeaderDecoration
            ?: return super.onInterceptTouchEvent(e)
        val pinnedHeaderRect: Rect? = pinnedHeaderInterface.pinnedHeaderRect
        val pinnedHeaderPosition: Int = pinnedHeaderInterface.pinnedHeaderPosition
        if (pinnedHeaderRect == null || pinnedHeaderPosition == -1) {
            return super.onInterceptTouchEvent(e)
        }
        when (e.action) {
            MotionEvent.ACTION_DOWN -> if (pinnedHeaderRect.contains(
                    e.x.toInt(),
                    e.y.toInt()
                )
            ) {
                return true
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    /**
     * 如果有固定的header的情况
     */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (mPinnedHeaderClickListener == null) {
            return super.onTouchEvent(ev)
        }
        val pinnedHeaderInterface: IPinnedHeaderDecoration = pinnedHeaderDecoration
            ?: return super.onTouchEvent(ev)
        val pinnedHeaderRect: Rect? = pinnedHeaderInterface.pinnedHeaderRect
        val pinnedHeaderPosition: Int = pinnedHeaderInterface.pinnedHeaderPosition
        if (pinnedHeaderRect == null || pinnedHeaderPosition == -1) {
            return super.onTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mPinnedHeaderHandle = false
                if (pinnedHeaderRect.contains(ev.x.toInt(), ev.y.toInt())) {
                    mPinnedHeaderHandle = true
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> if (mPinnedHeaderHandle) {
                return if (!pinnedHeaderRect.contains(ev.x.toInt(), ev.y.toInt())) {
                    val cancel = MotionEvent.obtain(ev)
                    cancel.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(cancel)
                    val down = MotionEvent.obtain(ev)
                    down.action = MotionEvent.ACTION_DOWN
                    super.dispatchTouchEvent(down)
                } else {
                    true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val x = ev.x
                val y = ev.y
                // 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发pinned header 点击
                if (mPinnedHeaderHandle && pinnedHeaderRect.contains(x.toInt(), y.toInt())) {
                    mPinnedHeaderClickListener!!.onPinnedHeaderClick(pinnedHeaderPosition)
                    mPinnedHeaderHandle = false
                    return true
                }
                mPinnedHeaderHandle = false
            }
            else -> {
            }
        }
        return super.onTouchEvent(ev)
    }

    private val pinnedHeaderDecoration: IPinnedHeaderDecoration?
        get() {
            var decorationIndex = 0
            var itemDecoration: ItemDecoration
            do {
                itemDecoration = getItemDecorationAt(decorationIndex)
                if (itemDecoration is IPinnedHeaderDecoration) {
                    return itemDecoration as IPinnedHeaderDecoration
                }
                decorationIndex++
            } while (itemDecoration != null)
            return null
        }
}