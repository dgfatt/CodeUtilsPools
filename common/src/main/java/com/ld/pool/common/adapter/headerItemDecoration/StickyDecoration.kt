package com.ld.pool.common.adapter.headerItemDecoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ld.pool.common.base.BaseDecoration
import com.ld.pool.common.listener.GroupListener
import com.ld.pool.common.listener.OnGroupClickListener
import com.ld.pool.data.entity.ClickInfoEntity
import kotlin.math.abs

class StickyDecoration(groupListener: GroupListener) : BaseDecoration() {
    @ColorInt
    private var mGroupTextColor = Color.WHITE//字体颜色，默认黑色
    private var mSideMargin = 10   //边距 左边距
    private var mTextSize = 50     //字体大小
    private var mGroupListener: GroupListener? = null

    private var mTextPaint: TextPaint
    private var mGroutPaint: Paint

    init {
        this.mGroupListener = groupListener
        //设置悬浮栏的画笔---mGroutPaint
        mGroutPaint = Paint()
        mGroutPaint.color = mGroupBackground
        //设置悬浮栏中文本的画笔
        mTextPaint = TextPaint()
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = mTextSize.toFloat()
        mTextPaint.color = mGroupTextColor
        mTextPaint.textAlign = Paint.Align.LEFT
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount = state.itemCount
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount) {
            val childView: View = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(childView)
            val realPosition = getRealPosition(position)
            if (isFirstInGroup(realPosition) || isFirstInRecyclerView(realPosition, i)) {
                //绘制悬浮
                var bottom =
                    mGroupHeight.coerceAtLeast((childView.top + parent.paddingTop))//决定当前顶部第一个悬浮Group的bottom
                if (position + 1 < itemCount) {
                    //下一组的第一个View接近头部
                    val viewBottom = childView.bottom
                    if (isLastLineInGroup(parent, realPosition) && viewBottom < bottom) {
                        bottom = viewBottom
                    }
                }
                drawDecoration(c, realPosition, left, right, bottom)
                if (mOnGroupClickListener != null) {
                    stickyHeaderPosArray[realPosition] = ClickInfoEntity(bottom)
                }
            } else {
                //绘制分割线
                drawDivide(c, parent, childView, realPosition, left, right)
            }
        }

    }

    /**
     * 绘制悬浮框
     *
     * @param c
     * @param realPosition
     * @param left
     * @param right
     * @param bottom
     */
    private fun drawDecoration(c: Canvas, realPosition: Int, left: Int, right: Int, bottom: Int) {
        val curGroupName: String      //当前item对应的Group
        val firstPositionInGroup = getFirstInGroupWithCash(realPosition)
        curGroupName = getGroupName(firstPositionInGroup)
        //根据top绘制group背景
        c.drawRect(
            left.toFloat(), (bottom - mGroupHeight).toFloat(),
            right.toFloat(), bottom.toFloat(), mGroutPaint
        )
        val fm: Paint.FontMetrics = mTextPaint.fontMetrics
        //文字竖直居中显示
        val baseLine = bottom - (mGroupHeight - (fm.bottom - fm.top)) / 2 - fm.bottom
        //获取文字宽度
        mSideMargin = abs(mSideMargin)
        c.drawText(curGroupName, (left + mSideMargin).toFloat(), baseLine, mTextPaint)
    }

    /**
     * 获取组名
     *
     * @param realPosition realPosition
     * @return 组名
     */
    override fun getGroupName(realPosition: Int): String {
        return mGroupListener?.getGroupName(realPosition)!!
    }


    class Builder(groupListener: GroupListener) {

        private var mDecoration: StickyDecoration = StickyDecoration(groupListener)

        companion object {
            fun init(groupListener: GroupListener): Builder {
                return Builder(groupListener)
            }
        }

        /**
         * 设置Group背景
         *
         * @param background 背景色
         */
        fun setGroupBackground(@ColorInt background: Int): Builder {
            mDecoration.mGroupBackground = background
            mDecoration.mGroutPaint.color = mDecoration.mGroupBackground
            return this
        }

        /**
         * 设置字体大小
         *
         * @param textSize 字体大小
         */
        fun setGroupTextSize(textSize: Int): Builder {
            mDecoration.mTextSize = textSize
            mDecoration.mTextPaint.textSize = mDecoration.mTextSize.toFloat()
            return this
        }

        /**
         * 设置Group高度
         *
         * @param groutHeight 高度
         * @return this
         */
        fun setGroupHeight(groutHeight: Int): Builder {
            mDecoration.mGroupHeight = groutHeight
            return this
        }

        /**
         * 组TextColor
         *
         * @param color 颜色
         * @return this
         */
        fun setGroupTextColor(@ColorInt color: Int): Builder {
            mDecoration.mGroupTextColor = color
            mDecoration.mTextPaint.color = mDecoration.mGroupTextColor
            return this
        }

        /**
         * 设置边距
         * 靠左时为左边距  靠右时为右边距
         *
         * @param leftMargin 左右距离
         * @return this
         */
        fun setTextSideMargin(leftMargin: Int): Builder {
            mDecoration.mSideMargin = leftMargin
            return this
        }

        /**
         * 设置分割线高度
         *
         * @param height 高度
         * @return this
         */
        fun setDivideHeight(height: Int): Builder {
            mDecoration.mDivideHeight = height
            return this
        }

        /**
         * 设置分割线颜色
         *
         * @param color color
         * @return this
         */
        fun setDivideColor(@ColorInt color: Int): Builder {
            mDecoration.mDivideColor = color
            mDecoration.mDividePaint.color = color
            return this
        }

        /**
         * 设置点击事件
         *
         * @param listener 点击事件
         * @return this
         */
        fun setOnClickListener(listener: OnGroupClickListener): Builder {
            mDecoration.setOnGroupClickListener(listener)
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
            mDecoration.resetSpan(recyclerView, gridLayoutManager)
            return this
        }

        /**
         * 设置头部数量
         * 用于顶部Header不需要设置悬浮的情况（仅LinearLayoutManager）
         * @param headerCount
         * @return
         */
        fun setHeaderCount(headerCount: Int): Builder {
            if (headerCount >= 0) {
                mDecoration.mHeaderCount = headerCount
            }
            return this
        }

        fun build(): StickyDecoration {
            return mDecoration
        }
    }
}