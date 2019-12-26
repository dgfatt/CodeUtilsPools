package com.ld.pool.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ld.pool.common.R
import com.ld.pool.common.listener.IStick


/**
 * 悬浮布局封装
 */
class StickFrameLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    FrameLayout(context!!, attrs, defStyleAttr) {
    private var mRecyclerView: RecyclerView? = null
    // 悬浮根布局
    private var mStickyLayout: FrameLayout? = null
    // 要悬浮的布局
    private var mStickView: View? = null
    // 偏移量
    private var mOffset = 0

    /**
     * 1. 加载布局完成之后
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        // 添加监听
        addOnScrollListener()
        // 添加悬浮根布局
        addStickyLayout()
    }

    /**
     * 添加滚动监听
     */
    private fun addOnScrollListener() {
        mRecyclerView = getChildAt(0) as RecyclerView
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                this@StickFrameLayout.onScrolled()
            }
        })
    }

    /**
     * 滚动监听事件处理
     */
    private fun onScrolled() {
        val adapter = mRecyclerView!!.adapter
        val layoutManager = mRecyclerView!!.layoutManager
        if (adapter == null || layoutManager == null || adapter.itemCount <= 0) {
            return
        }
        if (adapter is IStick) {
            val stick: IStick = adapter as IStick
            val stickPosition: Int = stick.stickPosition
            if (mStickView == null) { // 根据类型创建ViewHolder
                mStickyLayout!!.setTag(R.id.view_position, stickPosition)
                val viewHolder =
                    adapter.onCreateViewHolder(mStickyLayout!!, stick.stickViewType)
                // 根据位置绑定View
                adapter.onBindViewHolder(viewHolder, stickPosition)
                mStickView = viewHolder.itemView
                mStickyLayout!!.addView(mStickView)
            }
            //这是是处理第一次打开时，吸顶布局已经添加到StickyLayout，但StickyLayout的高依然为0的情况。
            if (mStickyLayout!!.childCount > 0 && mStickyLayout!!.height == 0) {
                mStickyLayout!!.requestLayout()
            }
            //设置StickyLayout显示或者隐藏。
            val firstVisibleItemPosition = findFirstVisibleItemPosition(mRecyclerView)
            val topView = layoutManager.findViewByPosition(stickPosition)
            // 1. 判断要不要偏移
            changeOffset(mOffset)
            // 1. 大于悬浮的位置都显示
            if (firstVisibleItemPosition >= stickPosition) {
                mStickyLayout!!.visibility = View.VISIBLE
            } else if (topView != null) { // 2. 偏移大于悬浮到顶部的距离就显示
                val isShow = mOffset >= topView.top
                if (isShow) {
                    mStickyLayout!!.visibility = View.VISIBLE
                } else {
                    mStickyLayout!!.visibility = View.GONE
                }
            } else {
                mStickyLayout!!.visibility = View.GONE
            }
        }
    }

    /**
     * 手动设置显示
     *
     * @param visible
     */
    fun setStickyVisibility(visible: Int) {
        if (mStickyLayout != null) {
            mStickyLayout!!.visibility = visible
        }
    }

    /**
     * 找第一个可见条目的位置
     */
    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView?): Int {
        var firstVisibleItem = -1
        val layout = recyclerView!!.layoutManager
        if (layout != null) {
            if (layout is GridLayoutManager) {
                firstVisibleItem = layout.findFirstVisibleItemPosition()
            } else if (layout is LinearLayoutManager) {
                firstVisibleItem = layout.findFirstVisibleItemPosition()
            } else if (layout is StaggeredGridLayoutManager) {
                val firstPositions =
                    IntArray(layout.spanCount)
                layout.findFirstVisibleItemPositions(firstPositions)
                firstVisibleItem = getMin(firstPositions)
            }
        }
        return firstVisibleItem
    }

    private fun getMin(arr: IntArray): Int {
        var min = arr[0]
        for (x in 1 until arr.size) {
            if (arr[x] < min) min = arr[x]
        }
        return min
    }

    /**
     * 添加悬浮根布局
     */
    private fun addStickyLayout() {
        mStickyLayout = FrameLayout(context)
        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        mStickyLayout!!.layoutParams = lp
        super.addView(mStickyLayout, lp)
    }

    /**
     * 设置偏移量
     */
    fun setStickOffset(offset: Int) {
        changeOffset(offset)
    }

    /**
     * 改变偏移量
     */
    private fun changeOffset(offset: Int) {
        if (mOffset != offset) {
            if (mStickyLayout != null) {
                mOffset = offset
                val lp =
                    mStickyLayout!!.layoutParams as LayoutParams
                lp.topMargin = offset
                mStickyLayout!!.layoutParams = lp
            }
        }
    }

    override fun computeVerticalScrollOffset(): Int {
        if (mRecyclerView != null) {
            try {
                val method =
                    View::class.java.getDeclaredMethod("computeVerticalScrollOffset")
                method.isAccessible = true
                return method.invoke(mRecyclerView) as Int
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.computeVerticalScrollOffset()
    }

    override fun computeVerticalScrollRange(): Int {
        if (mRecyclerView != null) {
            try {
                val method =
                    View::class.java.getDeclaredMethod("computeVerticalScrollRange")
                method.isAccessible = true
                return method.invoke(mRecyclerView) as Int
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.computeVerticalScrollRange()
    }

    override fun computeVerticalScrollExtent(): Int {
        if (mRecyclerView != null) {
            try {
                val method =
                    View::class.java.getDeclaredMethod("computeVerticalScrollExtent")
                method.isAccessible = true
                return method.invoke(mRecyclerView) as Int
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.computeVerticalScrollExtent()
    }

    override fun scrollBy(x: Int, y: Int) {
        if (mRecyclerView != null) {
            mRecyclerView!!.scrollBy(x, y)
        } else {
            super.scrollBy(x, y)
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        if (mRecyclerView != null) {
            mRecyclerView!!.scrollTo(x, y)
        } else {
            super.scrollTo(x, y)
        }
    }
}