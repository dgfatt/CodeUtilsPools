package com.ld.pool.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.ld.pool.common.R


open class BackArrowView(
    context: Context, @Nullable attrs: AttributeSet?,
    defStyleAttr: Int
) :
    View(context, attrs, defStyleAttr) {
    /**
     * 控件宽
     */
    private var mViewWidth = 0
    /**
     * 控件高
     */
    private var mViewHeight = 0
    /**
     * 箭头开始的距离
     */
    private var mArrowStartDistance = 0f
    /**
     * 箭头的2个边的长度
     */
    private var mArrowLineLength = 0f
    /**
     * 箭头颜色
     */
    private var mColor = 0
    /**
     * 箭头粗细
     */
    private var mArrowStrokeWidth = 0f
    /**
     * 风格模式
     */
    private var mArrowStyle = 0
    /**
     * 画笔
     */
    private var mPaint: Paint? = null
    /**
     * 箭头Path
     */
    private var mArrowPath: Path? = null

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun init(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        initAttr(context, attrs, defStyleAttr)
        mPaint = Paint()
        mPaint!!.color = mColor
        //使用Path必须使用STROKE，使用FILL是画不了的
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.isAntiAlias = true
        mPaint!!.strokeWidth = mArrowStrokeWidth
        //设置拐角形状为圆形，3条线相接处则不会有缝隙了
        mPaint!!.strokeJoin = Paint.Join.ROUND
    }

    private fun initAttr(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val array: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BackArrowView, defStyleAttr, 0)
        mColor = array.getColor(R.styleable.BackArrowView_bav_color, Color.argb(255, 0, 0, 0))
        mArrowStrokeWidth = array.getDimension(
            R.styleable.BackArrowView_bav_stroke_width,
            dip2px(context, 2f).toFloat()
        )
        mArrowStyle = array.getInt(
            R.styleable.BackArrowView_bav_arrow_style,
            ARROW_STYLE_MATERIAL_DESIGN
        )
        array.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算半径
        val radius = mViewWidth.coerceAtMost(mViewHeight) / 2f
        //计算箭头起始位置
        if (mArrowStyle == ARROW_STYLE_MATERIAL_DESIGN) {
            mArrowStartDistance = radius / 3f
        } else if (mArrowStyle == ARROW_STYLE_WECHAT_DESIGN) {
            mArrowStartDistance = radius / 4f
        }
        //计算箭头长度
        mArrowLineLength = radius * 0.63f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将画布中心移动到中心点偏左位置
        canvas.translate(mViewWidth / 2f - mArrowStartDistance, (mViewHeight / 2).toFloat())
        //将画布旋转45度，让后面画的直角旋转
        canvas.rotate(45F)
        if (mArrowPath == null) {
            mArrowPath = Path()
        }
        mArrowPath!!.reset()
        //画第一条线
        mArrowPath!!.lineTo(0F, -mArrowLineLength)
        //画第二条线
        mArrowPath!!.moveTo(0F, 0F)
        mArrowPath!!.lineTo(mArrowLineLength, 0F)
        //Google Material Design风格才有中间的线
        if (mArrowStyle == ARROW_STYLE_MATERIAL_DESIGN) { //画中间的线
            mArrowPath!!.moveTo(0F, 0F)
            mArrowPath!!.lineTo(mArrowLineLength, -mArrowLineLength)
        }
        //闭合路径
        mArrowPath!!.close()
        //画路径
        canvas.drawPath(mArrowPath!!, mPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(handleMeasure(widthMeasureSpec), handleMeasure(heightMeasureSpec))
    }

    /**
     * 处理MeasureSpec
     */
    private fun handleMeasure(measureSpec: Int): Int {
        var result = DEFAULT_MIN_WIDTH
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else { //处理wrap_content的情况
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    companion object {
        /**
         * View默认最小宽度
         */
        private const val DEFAULT_MIN_WIDTH = 100
        /**
         * Material Design风格
         */
        private const val ARROW_STYLE_MATERIAL_DESIGN = 1
        /**
         * 微信风格
         */
        private const val ARROW_STYLE_WECHAT_DESIGN = 2

        fun dip2px(context: Context, dipValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}