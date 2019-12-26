package com.ld.pool.common.adapter.headerItemDecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.blankj.utilcode.util.LogUtils
import com.ld.pool.common.R
import com.ld.pool.data.bean.DetailBean


class ItemHeaderDecoration(
    context: Context,
    dataList: List<DetailBean>
) :
    ItemDecoration() {

    private val mContext: Context = context
    private val mList: List<DetailBean> = dataList
    private val mLayoutInflater: LayoutInflater
    private var mTitleHight = 50
    private val mTitleTextSize = 20

    companion object {

        private var currentTag: String? = "0"

        fun setCurrentTag(tag: String?) {
            currentTag = tag
        }

    }

    init {
        mTitleHight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            50f,
            context.resources.displayMetrics
        ).toInt()
        // mTitleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //获取到视图中第一个可见的item的position
        val position =
            (parent.layoutManager as GridLayoutManager?)!!.findFirstVisibleItemPosition()
        val tag = mList[position].tag
        val child: View = parent.findViewHolderForLayoutPosition(position)!!.itemView
        var flag = false

        if (position + 1 < mList.size) {
            val suspensionTag = mList[position + 1].tag
            if (null != tag && tag != suspensionTag) {
                LogUtils.d(
                    "ZHG-TEST",
                    "!!!!!!!!!!!!!child.Height() = " + child.height.toString() + " , child.top = " + child.top.toString() + " , mTitleHight = " + mTitleHight
                )
                if (child.height + child.top < mTitleHight) {
                    c.save()
                    flag = true
                    c.translate(0F, (child.height + child.top - mTitleHight).toFloat())
                }
            }
        }

        val topTitleView: View = mLayoutInflater.inflate(R.layout.item_detail_title, parent, false)
        val textView = topTitleView.findViewById(R.id.title_textview) as TextView
        //textView.setTextSize(mTitleTextSize);
        textView.text = "分类$tag"
        val toDrawWidthSpec: Int //用于测量的widthMeasureSpec
        val toDrawHeightSpec: Int //用于测量的heightMeasureSpec
        val lp =
            topTitleView.layoutParams as RecyclerView.LayoutParams
        topTitleView.layoutParams = lp

        toDrawWidthSpec =
            when (lp.width) {
                ViewGroup.LayoutParams.MATCH_PARENT -> { //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
                    View.MeasureSpec.makeMeasureSpec(
                        parent.width - parent.paddingLeft - parent.paddingRight,
                        View.MeasureSpec.EXACTLY
                    )
                }
                ViewGroup.LayoutParams.WRAP_CONTENT -> { //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
                    View.MeasureSpec.makeMeasureSpec(
                        parent.width - parent.paddingLeft - parent.paddingRight,
                        View.MeasureSpec.AT_MOST
                    )
                }
                else -> { //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
                    View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY)
                }
            }

        //高度同理
        toDrawHeightSpec = when (lp.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> {
                View.MeasureSpec.makeMeasureSpec(
                    parent.height - parent.paddingTop - parent.paddingBottom,
                    View.MeasureSpec.EXACTLY
                )
            }
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                View.MeasureSpec.makeMeasureSpec(
                    parent.height - parent.paddingTop - parent.paddingBottom,
                    View.MeasureSpec.AT_MOST
                )
            }
            else -> {
                View.MeasureSpec.makeMeasureSpec(mTitleHight, View.MeasureSpec.EXACTLY)
            }
        }

        //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上。
        topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec)
        topTitleView.layout(
            parent.paddingLeft,
            parent.paddingTop,
            parent.paddingLeft + topTitleView.measuredWidth,
            parent.paddingTop + topTitleView.measuredHeight
        )

        //Canvas默认在视图顶部，无需平移，直接绘制
        topTitleView.draw(c)
        //恢复画布到之前保存的状态
        if (flag) c.restore()
        if (!TextUtils.equals(
                tag,
                currentTag
            )
        ) {
            currentTag = tag
        }
    }
}