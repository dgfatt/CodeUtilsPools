package com.ld.pool.common.base

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.Log
import android.util.SparseIntArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.data.entity.ClickInfoEntity


abstract class BaseDecoration : ItemDecoration() {
    // TODO: 2018/4/13 加载更新后闪动
    /**
     * group背景色，默认透明
     */
    @ColorInt
    var mGroupBackground: Int = Color.parseColor("#48BDFF")
    /**
     * 悬浮栏高度
     */
    var mGroupHeight = 120
    /**
     * 分割线颜色，默认灰色
     */
    @ColorInt
    var mDivideColor: Int = Color.parseColor("#CCCCCC")
    /**
     * 分割线宽度
     */
    var mDivideHeight = 0
    /**
     * RecyclerView头部数量
     * 最小为0
     */
    var mHeaderCount = 0
    var mDividePaint: Paint = Paint()
    /**
     * 缓存分组第一个item的position
     */
    private val firstInGroupCash = SparseIntArray(100)
    var mOnGroupClickListener: OnGroupClickListener? = null
    /**
     * 设置点击事件
     *
     * @param listener
     */
    protected fun setOnGroupClickListener(listener: OnGroupClickListener?) {
        mOnGroupClickListener = listener
    }

    /**
     * 获取分组名
     *
     * @param realPosition realPosition
     * @return group
     */
    abstract fun getGroupName(realPosition: Int): String?

    /**
     * 获取真实的position
     * @param position
     * @return
     */
    protected fun getRealPosition(position: Int): Int {
        return position - mHeaderCount
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val realPosition = getRealPosition(position)
        val manager = parent.layoutManager
        if (manager is GridLayoutManager) { //网格布局
            val spanCount = manager.spanCount
            if (!isHeader(realPosition)) {
                if (isFirstLineInGroup(realPosition, spanCount)) { //为悬浮view预留空间
                    outRect.top = mGroupHeight
                } else { //为分割线预留空间
                    outRect.top = mDivideHeight
                }
            }
        } else { //其他的默认为线性布局
            //只有是同一组的第一个才显示悬浮栏
            if (!isHeader(realPosition)) {
                if (isFirstInGroup(realPosition)) { //为悬浮view预留空间
                    outRect.top = mGroupHeight
                } else { //为分割线预留空间
                    outRect.top = mDivideHeight
                }
            }
        }
    }

    /**
     * 判断是不是组中的第一个位置
     * 根据前一个组名，判断当前是否为新的组
     * 当前为groupId为null时，则与上一个为同一组
     */
    fun isFirstInGroup(realPosition: Int): Boolean {
        if (realPosition < 0) { //小于header数量，不是第一个
            return false
        } else if (realPosition == 0) { //等于header数量，为第一个
            return true
        }
        val preGroupId: String? = if (realPosition <= 0) {
            null
        } else {
            getGroupName(realPosition - 1)
        }
        val curGroupId = getGroupName(realPosition) ?: return false
        return !TextUtils.equals(preGroupId, curGroupId)
    }

    /**
     * 是否在RecyclerView处于第一个（header部分不算）
     *
     * @param realPosition 总的position
     * @param index    RecyclerView中的Index
     * @return
     */
    protected fun isFirstInRecyclerView(realPosition: Int, index: Int): Boolean {
        return realPosition >= 0 && index == 0
    }

    /**
     * 是否为Header
     *
     */
    private fun isHeader(realPosition: Int): Boolean {
        return realPosition < 0
    }

    /**
     * 判断是不是新组的第一行（GridLayoutManager使用）
     * 利用当前行的第一个对比前一个组名，判断当前是否为新组的第一样
     */
    private fun isFirstLineInGroup(realPosition: Int, spanCount: Int): Boolean {
        return if (realPosition < 0) { //小于header数量，不是第一个
            false
        } else if (realPosition == 0) {
            true
        } else {
            val posFirstInGroup = getFirstInGroupWithCash(realPosition)
            realPosition - posFirstInGroup < spanCount
        }
    }

    /**
     * 网格布局需要调用
     *
     * @param recyclerView      recyclerView
     * @param gridLayoutManager gridLayoutManager
     */
    fun resetSpan(recyclerView: RecyclerView?, gridLayoutManager: GridLayoutManager?) {
        if (recyclerView == null) {
            throw NullPointerException("recyclerView not allow null")
        }
        if (gridLayoutManager == null) {
            throw NullPointerException("gridLayoutManager not allow null")
        }
        val spanCount = gridLayoutManager.spanCount
        //相当于weight
        val lookup: GridLayoutManager.SpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val span: Int
                val realPosition = getRealPosition(position)
                if (realPosition < 0) { //小于header数量
                    span = spanCount
                } else {
                    val curGroupId = getGroupName(realPosition)
                    val nextGroupId: String?
                    nextGroupId = try { //防止外面没判断，导致越界
                        getGroupName(realPosition + 1)
                    } catch (e: Exception) {
                        curGroupId
                    }
                    span = if (!TextUtils.equals(curGroupId, nextGroupId)) { //为本行的最后一个
                        val posFirstInGroup = getFirstInGroupWithCash(realPosition)
                        spanCount - (realPosition - posFirstInGroup) % spanCount
                    } else {
                        1
                    }
                }
                return span
            }
        }
        gridLayoutManager.spanSizeLookup = lookup
    }

    /**
     * down事件在顶部悬浮栏中
     */
    private var mDownInHeader = false

    /**
     * RecyclerView onInterceptEvent中down事件调用，用于处理点击穿透问题
     *
     * @param event
     */
    fun onEventDown(event: MotionEvent?) {
        if (event == null) {
            mDownInHeader = false
            return
        }
        mDownInHeader = event.y > 0 && event.y < mGroupHeight
    }

    /**
     * RecyclerView onInterceptEvent中up事件调用，用于处理点击穿透问题
     *
     * @param event
     * @return
     */
    fun onEventUp(event: MotionEvent): Boolean {
        if (mDownInHeader) {
            val y = event.y
            val isInHeader = y > 0 && y < mGroupHeight
            if (isInHeader) {
                return onTouchEvent(event)
            }
        }
        return false
    }

    /**
     * 得到当前分组第一个item的position
     *
     * @param realPosition realPosition
     */
    protected fun getFirstInGroupWithCash(realPosition: Int): Int {
        return getFirstInGroup(realPosition)
    }

    /**
     * 得到当前分组第一个item的position
     *
     * @param realPosition realPosition
     */
    private fun getFirstInGroup(realPosition: Int): Int {
        return if (realPosition <= 0) {
            0
        } else {
            if (isFirstInGroup(realPosition)) {
                realPosition
            } else {
                getFirstInGroup(realPosition - 1)
            }
        }
    }

    /**
     * 判断自己是否为group的最后一行
     *
     * @param recyclerView recyclerView
     * @param realPosition     realPosition
     * @return
     */
    protected fun isLastLineInGroup(recyclerView: RecyclerView, realPosition: Int): Boolean {
        return if (realPosition < 0) {
            true
        } else {
            val curGroupName = getGroupName(realPosition)
            val nextGroupName: String?
            val manager = recyclerView.layoutManager
            //默认往下查找的数量
            var findCount = 1
            if (manager is GridLayoutManager) {
                val spanCount = manager.spanCount
                val firstPositionInGroup = getFirstInGroupWithCash(realPosition)
                findCount = spanCount - (realPosition - firstPositionInGroup) % spanCount
            }
            nextGroupName = try {
                getGroupName(realPosition + findCount)
            } catch (e: Exception) {
                curGroupName
            }
            if (nextGroupName == null) {
                true
            } else !TextUtils.equals(curGroupName, nextGroupName)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //点击事件处理
        if (gestureDetector == null) {
            gestureDetector = GestureDetector(parent.context, gestureListener)
            parent.setOnTouchListener { _, event -> gestureDetector?.onTouchEvent(event)!! }
        }
        stickyHeaderPosArray.clear()
    }

    /**
     * 点击事件调用
     *
     * @param realPosition realPosition
     */
    private fun onGroupClick(realPosition: Int, viewId: Int) {
        mOnGroupClickListener?.onClick(realPosition, viewId)
    }

    /**
     * 记录每个头部和悬浮头部的坐标信息【用于点击事件】
     * 位置由子类添加
     */
    var stickyHeaderPosArray: HashMap<Int, ClickInfoEntity> = HashMap()
    private var gestureDetector: GestureDetector? = null
    private val gestureListener: GestureDetector.OnGestureListener =
        object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent?): Boolean {
                return false
            }

            override fun onShowPress(e: MotionEvent?) {}

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return onTouchEvent(e)
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                return false
            }

            override fun onLongPress(e: MotionEvent?) {}
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return false
            }
        }

    /**
     * 对touch事件处理，找到点击事件
     *
     * @param e
     * @return
     */
    private fun onTouchEvent(e: MotionEvent?): Boolean {
        for (key in stickyHeaderPosArray.keys) {
            val value: ClickInfoEntity? = stickyHeaderPosArray[key]
            val y = e?.y
            val x = e?.x
            if (value!!.mBottom - mGroupHeight <= y!! && y <= value.mBottom) { //如果点击到分组头
                if (value.mDetailInfoList == null || value.mDetailInfoList!!.size === 0) { //没有子View的点击事件
                    onGroupClick(key, value.mGroupId)
                } else {
                    val list: MutableList<ClickInfoEntity.DetailInfo> = value.mDetailInfoList!!
                    var isChildViewClicked = false
                    for (detailInfo in list) {
                        if (detailInfo.top <= y && y <= detailInfo.bottom && detailInfo.left <= x!! && detailInfo.right >= x
                        ) { //当前view被点击
                            onGroupClick(key, detailInfo.id)
                            isChildViewClicked = true
                            break
                        }
                    }
                    if (!isChildViewClicked) { //点击范围不在带有id的子view中，则表示整个groupView被点击
                        onGroupClick(key, value.mGroupId)
                    }
                }
                return true
            }
        }
        return false
    }

    /**
     * 绘制分割线
     *
     * @param c
     * @param parent
     * @param childView
     * @param realPosition
     * @param left
     * @param right
     */
    protected fun drawDivide(
        c: Canvas,
        parent: RecyclerView,
        childView: View,
        realPosition: Int,
        left: Int,
        right: Int
    ) {
        if (mDivideHeight != 0 && !isHeader(realPosition)) {
            val manager = parent.layoutManager
            if (manager is GridLayoutManager) {
                val spanCount = manager.spanCount
                if (!isFirstLineInGroup(realPosition, spanCount)) {
                    val bottom: Float = (childView.top + parent.paddingTop).toFloat()
                    //高度小于顶部悬浮栏时，跳过绘制
                    if (bottom >= mGroupHeight) {
                        c.drawRect(
                            left.toFloat(), bottom - mDivideHeight,
                            right.toFloat(), bottom, mDividePaint
                        )
                    }
                }
            } else {
                val bottom: Float = childView.top.toFloat()
                //高度小于顶部悬浮栏时，跳过绘制
                if (bottom >= mGroupHeight) {
                    c.drawRect(
                        left.toFloat(), bottom - mDivideHeight,
                        right.toFloat(), bottom, mDividePaint
                    )
                }
            }
        }
    }

    protected fun log(content: String?) {
        if (false) {
            Log.i("StickDecoration", content)
        }
    }

    init {
        mDividePaint.setColor(mDivideColor)
    }
}