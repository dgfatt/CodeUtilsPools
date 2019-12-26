package com.ld.pool.common.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ld.pool.common.R
import kotlinx.android.synthetic.main.layout_view_step.view.*

/**
 * 物流进度的View
 * @author Administrator
 */
class StepProgressLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var mFirstImage = 0
    private var mFirstImageSize = 0
    private var mNormalImage = 0
    private var mNormalImageSize = 0
    private var mLineSize = 0
    private var mLineColor = Color.parseColor("#e6e6e6")
    private var mFirstLineHeight = 0
    private var mNormalLineHeight = 0
    /**
     * 进度样式
     */
    private var mStepStyle = STYLE_NORMAL
    private var mIcon: ImageView? = null
    private fun obtainStyledAttributes(
        context: Context,
        attrs: AttributeSet?
    ) {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.StepProgressLayout)
        mFirstImage = array.getResourceId(R.styleable.StepProgressLayout_step_first_image, 0)
        mFirstImageSize = array.getDimension(
            R.styleable.StepProgressLayout_step_first_image_size,
            dip2px(20).toFloat()
        ).toInt()
        mNormalImage = array.getResourceId(R.styleable.StepProgressLayout_step_normal_image, 0)
        mNormalImageSize = array.getDimension(
            R.styleable.StepProgressLayout_step_normal_image_size,
            dip2px(11).toFloat()
        ).toInt()
        mLineSize = array.getDimension(R.styleable.StepProgressLayout_step_line_size, 1f).toInt()
        mLineColor = array.getColor(R.styleable.StepProgressLayout_step_line_color, mLineColor)
        mFirstLineHeight = array.getDimension(
            R.styleable.StepProgressLayout_step_first_line_height,
            dip2px(20).toFloat()
        ).toInt()
        mNormalLineHeight = array.getDimension(
            R.styleable.StepProgressLayout_step_normal_line_height,
            dip2px(20).toFloat()
        ).toInt()
        array.recycle()
    }

    private fun initViews() {
        mIcon = findViewById(R.id.step_icon)
        if (mStepStyle == STYLE_FIRST) {
            setImageResource(mFirstImage)
            setImageSize(mFirstImageSize)
            step_top_line.visibility = View.INVISIBLE
        } else if (mStepStyle == STYLE_NORMAL || mStepStyle == STYLE_END) {
            setImageResource(mNormalImage)
            setImageSize(mNormalImageSize)
            if (mStepStyle == STYLE_END) {
                step_bottom_line.visibility = View.INVISIBLE
            }
        }
        setTopLineHeight(mFirstLineHeight)
        setLineColor(mLineColor)
        setLineSize(mLineSize)
    }

    fun setLineColor(color: Int) {
        if (color != 0) {
            step_bottom_line.setBackgroundColor(color)
            step_top_line!!.setBackgroundColor(color)
        }
    }

    /**
     * 设置第一条线第一个条目的高度
     */
    fun setFirstLineHeight(height: Int) {
        mFirstLineHeight = height
    }

    /**
     * 设置第一条线正常样式的高度
     */
    fun setNormalLineHeight(height: Int) {
        mNormalLineHeight = height
    }

    fun setTopLineHeight(height: Int) {
        if (height != 0) {
            val params =
                step_top_line!!.layoutParams as LayoutParams
            params.height = height
            step_top_line!!.layoutParams = params
        }
    }

    /**
     * 设置线的大小
     */
    fun setLineSize(width: Int) {
        mLineSize = width
        if (mLineSize != 0) {
            var params =
                step_top_line!!.layoutParams as LayoutParams
            params.width = width
            step_top_line!!.layoutParams = params
            params = step_bottom_line!!.layoutParams as LayoutParams
            params.width = width
            step_bottom_line!!.layoutParams = params
        }
    }

    /**
     * 设置第一个图片资源
     */
    fun setHeadImageSize(size: Int) {
        mFirstImageSize = size
        setImageSize(mFirstImageSize)
    }

    private fun setImageResource(resource: Int) {
        if (resource != 0) {
            mIcon!!.setImageResource(resource)
        }
    }

    private fun setImageSize(size: Int) {
        if (size >= 0) {
            val params =
                mIcon!!.layoutParams as LayoutParams
            params.height = size
            params.width = size
            mIcon!!.layoutParams = params
        }
    }

    /**
     * 变成第一个条目样式
     */
    fun changeFirstStyle() {
        if (mStepStyle != STYLE_FIRST) {
            mStepStyle = STYLE_FIRST
            step_top_line!!.visibility = View.INVISIBLE
            setImageResource(mFirstImage)
            setHeadImageSize(mFirstImageSize)
            setTopLineHeight(mFirstLineHeight)
        }
    }

    /**
     * 变成正常条目样式
     */
    fun changeNormalStyle() {
        if (mStepStyle != STYLE_NORMAL) {
            mStepStyle = STYLE_NORMAL
            step_bottom_line!!.visibility = View.VISIBLE
            step_top_line!!.visibility = View.VISIBLE
            setImageResource(mNormalImage)
            setImageSize(mNormalImageSize)
            setTopLineHeight(mFirstLineHeight)
        }
    }

    /**
     * 最后一种样式
     */
    fun changeEndStyle() {
        if (mStepStyle != STYLE_END) {
            mStepStyle = STYLE_END
            step_top_line!!.visibility = View.VISIBLE
            step_bottom_line!!.visibility = View.INVISIBLE
            setImageResource(mNormalImage)
            setImageSize(mNormalImageSize)
            setTopLineHeight(mFirstLineHeight)
        }
    }

    private fun dip2px(dip: Int): Int {
        return (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip.toFloat(),
            resources.displayMetrics
        ) + 0.5).toInt()
    }

    companion object {
        /**
         * 第一个条目样式
         */
        private const val STYLE_FIRST = 0
        /**
         * 正常条目样式
         */
        private const val STYLE_NORMAL = 1
        /**
         * 最后一条条目样式
         */
        private const val STYLE_END = 2
    }

    init {
        // 加载布局
        View.inflate(context, R.layout.layout_view_step, this)
        // 加载自定义的属性
        obtainStyledAttributes(context, attrs)
        // 初始化View
        initViews()
    }
}