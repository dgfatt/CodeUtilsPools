package com.ld.pool.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * RecyclerView -ViewPage-RecyclerView  解决冲突
 */
class RVVPRecyclerView : RecyclerView {

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
    }

    private var downX //按下时 的X坐标
            = 0f
    private var downY //按下时 的Y坐标
            = 0f
    private var maxY = 0
    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        val x = e.x
        val y = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //将按下时的坐标存储
                downX = x
                downY = y
                //                True if the child does not want the parent to
//                intercept touch events.
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                //获取到距离差
                val dx = x - downX
                val dy = y - downY
                Log.d("aaa", "ACTION_MOVE")
                //通过距离差判断方向
                val orientation = getOrientation(dx, dy)
                val location = intArrayOf(0, 0)
                getLocationOnScreen(location)
                when (orientation) {
                    'd' -> if (canScrollVertically(-1)) {
                        Log.d("aaa", "b   不要拦截")
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else { //内层RecyclerView下拉到最顶部时候不再处理事件
                        parent.requestDisallowInterceptTouchEvent(false)
                        Log.d("aaa", "b  拦截")
                    }
                    'u' -> {
                        Log.d("aaa", "maxY$maxY")
                        Log.d("aaa", "location[1]" + location[1] + "")
                        if (location[1] <= maxY) {
                            parent.requestDisallowInterceptTouchEvent(true)
                            Log.d("aaa", "u   不要拦截")
                        } else {
                            Log.d("aaa", "u   拦截")
                            parent.requestDisallowInterceptTouchEvent(false)
                            //                            不加这个返回虽然拦截了，当子list在最顶部的时候，上滑不会整体移动
                            return true
                        }
                    }
                    'r' -> {
                        Log.d("aaa", "r  拦截")
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                    'l' -> {
                        Log.d("aaa", "l  拦截")
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(e)
    }

    fun setMaxY(height: Int) {
        maxY = height
    }

    private fun getOrientation(dx: Float, dy: Float): Char {
        return if (abs(dx) > abs(dy)) { //X轴移动
            if (dx > 0) 'r' else 'l' //右,左
        } else { //Y轴移动
            if (dy > 0) 'd' else 'u' //下//上
        }
    }
}
