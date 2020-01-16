package com.ld.pool.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

/**
 * RecyclerView顶部阴影透明度渐变效果
 */
class FadingEdgeTopRecyclerView(context: Context, attrs: AttributeSet) :
    RecyclerView(context, attrs) {

    init {
        //列表是否垂直
        isVerticalFadingEdgeEnabled = true
        //阴影高度
        setFadingEdgeLength(SizeUtils.px2dp(100F))
    }

    /**
     * 顶部阴影强度，这里用系统的默认效果，所以没有重写
     */
    override fun getTopFadingEdgeStrength(): Float {
        return super.getTopFadingEdgeStrength()
    }

    /**
     * 底部阴影强度，这里不需要，所以设置为0f
     */
    override fun getBottomFadingEdgeStrength(): Float {
        return 0f
    }

}