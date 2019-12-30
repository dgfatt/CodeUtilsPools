package com.ld.pool.common.adapter.headerItemDecoration

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.common.base.BaseDecoration
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.common.listener.PowerGroupListener
import com.ld.pool.common.utils.CacheUtil
import com.ld.pool.common.utils.ViewUtil
import com.ld.pool.data.entity.ClickInfoEntity

/**
 * View悬浮
 * 利用分割线实现悬浮
 */
class PowerfulStickyDecoration(groupListener: PowerGroupListener) : BaseDecoration() {

    private var mGroutPaint: Paint = Paint()

    /**
     * 缓存图片
     */
    private var mBitmapCache: CacheUtil<Bitmap> = CacheUtil()

    /**
     * 缓存View
     */
    private var mHeadViewCache: CacheUtil<View> = CacheUtil()

    private var mGroupListener: PowerGroupListener = groupListener

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //绘制
        val itemCount = state.itemCount
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(childView)
            val realPosition = getRealPosition(position)
            if (isFirstInGroup(realPosition) || isFirstInRecyclerView(realPosition, i)) {
                val viewBottom = childView.bottom
                //top 决定当前顶部第一个悬浮Group的位置
                var bottom =
                    mGroupHeight.coerceAtLeast(childView.top + parent.paddingTop)
                if (position + 1 < itemCount) {
                    //下一组的第一个View接近头部
                    if (isLastLineInGroup(parent, realPosition) && viewBottom < bottom) {
                        bottom = viewBottom
                    }
                }
                drawDecoration(c, realPosition, left, right, bottom)
            } else {
                //绘制分割线
                drawDivide(c, parent, childView, realPosition, left, right)
            }
        }
    }

    /**
     * 绘制悬浮框
     *
     * @param c        Canvas
     * @param realPosition realPosition
     * @param left     left
     * @param right    right
     * @param bottom   bottom
     */
    private fun drawDecoration(c: Canvas, realPosition: Int, left: Int, right: Int, bottom: Int) {
        c.drawRect(
            left.toFloat(), (bottom - mGroupHeight).toFloat(),
            right.toFloat(), bottom.toFloat(), mGroutPaint
        )
        //根据position获取View
        val groupView: View
        val firstPositionInGroup = getFirstInGroupWithCash(realPosition)
        if (mHeadViewCache[firstPositionInGroup] == null) {
            groupView = getGroupView(firstPositionInGroup)
            measureAndLayoutView(groupView, left, right)
            mHeadViewCache.put(firstPositionInGroup, groupView)
        } else {
            groupView = mHeadViewCache[firstPositionInGroup]!!
        }
        val bitmap: Bitmap
        if (mBitmapCache[firstPositionInGroup] != null) {
            bitmap = mBitmapCache[firstPositionInGroup]!!
        } else {
            bitmap = Bitmap.createBitmap(groupView.drawingCache)
            mBitmapCache.put(firstPositionInGroup, bitmap)
        }
        c.drawBitmap(bitmap, left.toFloat(), (bottom - mGroupHeight).toFloat(), null)
        if (mOnGroupClickListener != null) {
            setClickInfo(groupView, left, bottom, realPosition)
        }
    }

    /**
     * 点击的位置信息
     *
     * @param groupView
     * @param left
     * @param bottom
     * @param realPosition
     */
    private fun setClickInfo(groupView: View, left: Int, bottom: Int, realPosition: Int) {
        val parentTop = bottom - mGroupHeight
        val list: MutableList<ClickInfoEntity.DetailInfo> = ArrayList()
        val viewList: MutableList<View> = ViewUtil.getChildViewWithId(groupView)
        for (view in viewList) {
            val top = view.top + parentTop
            val bottom1 = view.bottom + parentTop
            val left1 = view.left + left
            val right = view.right + left
            list.add(ClickInfoEntity.DetailInfo(view.id, left1, right, top, bottom1))
        }
        val clickInfo = ClickInfoEntity(bottom, list)
        clickInfo.mGroupId = groupView.id
        stickyHeaderPosArray[realPosition] = clickInfo
    }

    /**
     * 对view进行测量和布局
     *
     * @param groupView groupView
     * @param left      left
     * @param right     right
     */
    private fun measureAndLayoutView(groupView: View, left: Int, right: Int) {
        groupView.isDrawingCacheEnabled = true
        //手动对view进行测量，指定groupView的高度、宽度
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(right, mGroupHeight)
        groupView.layoutParams = layoutParams
        groupView.measure(
            View.MeasureSpec.makeMeasureSpec(right, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(mGroupHeight, View.MeasureSpec.EXACTLY)
        )
        groupView.layout(left, 0 - mGroupHeight, right, 0)
    }

    /**
     * 获取组View
     *
     * @param firstPositionInGroup
     * @return 组名
     */
    private fun getGroupView(firstPositionInGroup: Int): View {
        return mGroupListener.getGroupView(firstPositionInGroup)!!
    }

    /**
     * 获取组名
     *
     * @param realPosition realPosition
     * @return 组名
     */
    override fun getGroupName(realPosition: Int): String? {
        return mGroupListener.getGroupName(realPosition)
    }

    /**
     * 是否使用缓存
     *
     * @param b b
     */
    fun setCacheEnable(b: Boolean) {
        mHeadViewCache.isCacheable(b)
    }

    /**
     * 清空缓存
     */
    fun clearCache() {
        mHeadViewCache.clean()
        mBitmapCache.clean()
    }

    /**
     * 通知重新绘制
     * 使用场景：网络图片加载后调用
     *
     * @param recyclerView recyclerView
     * @param realPosition     realPosition
     */
    fun notifyRedraw(recyclerView: RecyclerView, viewGroup: View, realPosition: Int) {
        viewGroup.isDrawingCacheEnabled = false
        val firstPositionInGroup = getFirstInGroupWithCash(realPosition)
        mBitmapCache.remove(firstPositionInGroup)
        mHeadViewCache.remove(firstPositionInGroup)
        val left = recyclerView.paddingLeft
        val right = recyclerView.width - recyclerView.paddingRight
        measureAndLayoutView(viewGroup, left, right)
        mHeadViewCache.put(firstPositionInGroup, viewGroup)
        recyclerView.invalidate()
    }


    class Builder(listener: PowerGroupListener) {

        private var mDecoration: PowerfulStickyDecoration? = null

        init {
            mDecoration = PowerfulStickyDecoration(listener)
        }

        companion object {
            fun init(listener: PowerGroupListener): Builder {
                return Builder(listener)
            }
        }

        /**
         * 设置Group高度
         *
         * @param groutHeight 高度
         * @return this
         */
        fun setGroupHeight(groutHeight: Int): Builder {
            mDecoration?.mGroupHeight = groutHeight
            return this
        }


        /**
         * 设置Group背景
         *
         * @param background 背景色
         */
        fun setGroupBackground(@ColorInt background: Int): Builder {
            mDecoration?.mGroupBackground = background
            mDecoration?.mGroutPaint?.color = mDecoration?.mGroupBackground!!
            return this
        }

        /**
         * 设置分割线高度
         *
         * @param height 高度
         * @return this
         */
        fun setDivideHeight(height: Int): Builder {
            mDecoration?.mDivideHeight = height
            return this
        }

        /**
         * 设置分割线颜色
         *
         * @param color color
         * @return this
         */
        fun setDivideColor(@ColorInt color: Int): Builder {
            mDecoration?.mDivideColor = color
            mDecoration?.mDividePaint?.color = mDecoration?.mDivideColor!!
            return this
        }

        /**
         * 设置点击事件
         *
         * @param listener 点击事件
         * @return this
         */
        fun setOnClickListener(listener: OnGroupClickListener): Builder {
            mDecoration?.setOnGroupClickListener(listener)
            return this
        }

        /**
         * 重置span
         *
         * @param recyclerView      recyclerView
         * @param gridLayoutManager gridLayoutManager
         * @return this
         */
        fun resetSpan(
            recyclerView: RecyclerView,
            gridLayoutManager: GridLayoutManager
        ): Builder {
            mDecoration?.resetSpan(recyclerView, gridLayoutManager)
            return this
        }

        /**
         * 是否使用缓存
         *
         * @param b
         * @return
         */
        fun setCacheEnable(b: Boolean): Builder {
            mDecoration?.setCacheEnable(b)
            return this
        }

        /**
         * 设置头部数量
         * 用于顶部Header不需要设置悬浮的情况（仅LinearLayoutManager）
         *
         * @param headerCount
         * @return
         */
        fun setHeaderCount(headerCount: Int): Builder {
            if (headerCount >= 0) {
                mDecoration?.mHeaderCount = headerCount
            }
            return this
        }

        fun build(): PowerfulStickyDecoration {
            return mDecoration!!
        }
    }
}