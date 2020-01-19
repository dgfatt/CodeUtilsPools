package com.ld.pool.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.Nullable

import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * 解决viewpage  左右滑动会被误认为是上下滑动
 */
class VPRecyclerView : RecyclerView {
    /****
     * 滑动距离及坐标 归还父控件焦点
     */
    private var xDistance = 0f
    private var yDistance = 0f
    private var xLast = 0f
    private var yLast = 0f

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    yDistance = 0f
                    xDistance = yDistance
                }
                xLast = ev.x
                yLast = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = ev.x
                val curY = ev.y
                xDistance += abs(curX - xLast)
                yDistance += abs(curY - yLast)
                xLast = curX
                yLast = curY
                Log.i("bbb", "yDistance$yDistance")
                Log.i("bbb", "xDistance$xDistance")
                val orientation = getOrientation(xDistance, yDistance)
                if (orientation == 'r' || orientation == 'l') {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun getOrientation(dx: Float, dy: Float): Char {
        return if (abs(dx) > abs(dy)) { //X轴移动
            if (dx > 0) 'r' else 'l' //右,左
        } else { //Y轴移动
            if (dy > 0) 'd' else 'u' //下//上
        }
    }
}