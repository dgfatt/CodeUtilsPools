package com.ld.pool.common.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.ld.pool.common.R
import kotlin.math.cos
import kotlin.math.sin


open class RotateDotView(
    context: Context, @Nullable attrs: AttributeSet?,
    defStyleAttr: Int
) :
    View(context, attrs, defStyleAttr), Runnable {
    /**
     * 控件宽
     */
    private var mViewWidth = 0
    /**
     * 控件高
     */
    private var mViewHeight = 0
    /**
     * 画笔
     */
    private var mPaint: Paint? = null
    /**
     * 外接圆的半径
     */
    private var mCircleRadius = 0f
    /**
     * 起始点的颜色
     */
    private var mStartColor = 0
    /**
     * 终止点的颜色
     */
    private var mEndColor = 0
    /**
     * 一共多少个点
     */
    private var mDotCount = 0
    /**
     * 圆点半径
     */
    private var mDotRadius = 0f
    /**
     * 平均角度
     */
    private var mAngle = 0
    /**
     * 旋转角度，默认和平均角度一样
     */
    private var mRotateAngle = 0
    /**
     * 每个点的数据
     */
    private var mDots: ArrayList<Dot>? = null
    /**
     * 当前旋转到的角度
     */
    private var mCurrentAngle = 0
    /**
     * 是否自动开始
     */
    private var isAutoStart = false
    /**
     * 点的模式
     */
    private var mDotMode = 0

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun init(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        initAttr(context, attrs, defStyleAttr)
        mPaint = Paint()
        mPaint?.color = mStartColor
        mPaint?.style = Paint.Style.FILL
        mPaint?.isAntiAlias = true
        mPaint?.strokeWidth = mDotRadius
    }

    private fun initAttr(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val array: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RotateDotView, defStyleAttr, 0)
        mStartColor = array.getColor(
            R.styleable.RotateDotView_rdv_start_color,
            Color.argb(255, 180, 180, 180)
        )
        //如果不设置endColor，默认取startColor的30%透明度作为endColor
        mEndColor = array.getColor(
            R.styleable.RotateDotView_rdv_end_color,
            Color.argb(
                76,
                Color.red(mStartColor),
                Color.green(mStartColor),
                Color.blue(mStartColor)
            )
        )
        mDotCount = array.getInt(R.styleable.RotateDotView_rdv_dot_count, 8)
        mDotRadius = array.getDimension(
            R.styleable.RotateDotView_rdv_dot_radius,
            dip2px(context, 2.6f).toFloat()
        )
        isAutoStart = array.getBoolean(R.styleable.RotateDotView_rdv_auto_start, true)
        //计算平均角度，默认是360 / 点的数量，例如8个点，算出来的平均角度就是45度
        mAngle = TOTAL_ROTATION_ANGLE / mDotCount
        mRotateAngle = array.getInt(R.styleable.RotateDotView_rdv_rotate_angle, mAngle)
        //获取模式
        mDotMode = array.getInt(
            R.styleable.RotateDotView_rdv_dot_mode,
            MODE_NORMAL
        )
        array.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        mCircleRadius = mViewHeight.coerceAtMost(mViewWidth) / 2f * 0.8f
        mDots = generateDot()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将坐标系原点移动到画布正中心
        canvas.translate((mViewWidth / 2).toFloat(), (mViewHeight / 2).toFloat())
        canvas.rotate(mCurrentAngle.toFloat())
        for (dot in mDots!!) {
            mPaint?.color = dot.color
            canvas.drawCircle(dot.x, dot.y, dot.dotRadius, mPaint!!)
        }
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isAutoStart) {
            postDelayed(this, INTERVAL_TIME)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(this)
    }

    override fun run() {
        if (mCurrentAngle >= TOTAL_ROTATION_ANGLE) {
            mCurrentAngle -= TOTAL_ROTATION_ANGLE
        } else { //每次叠加一个圆点的角度，就不会觉得在圆圈转动，而是点在切换
            mCurrentAngle += mRotateAngle
        }
        invalidate()
        postDelayed(this, INTERVAL_TIME)
    }

    /**
     * 生成点
     */
    private fun generateDot(): ArrayList<Dot> { //创建颜色估值器
        val argbEvaluator = ArgbEvaluator()
        val points: ArrayList<Dot> = ArrayList()
        for (i in 0 until mDotCount) {
            val currentAngle = i * mAngle.toFloat()
            //三角函数，计算坐标，注意这里Math的三角函数方法，传入的是弧长，需要乘以Math.PI来将角度换算为弧长，再进行计算
            val x =
                (mCircleRadius * cos(currentAngle / 180 * Math.PI)).toFloat()
            val y =
                (mCircleRadius * sin(currentAngle / 180 * Math.PI)).toFloat()
            //估算颜色，计算每个点的颜色
            val fraction = currentAngle / TOTAL_ROTATION_ANGLE
            val color = argbEvaluator.evaluate(fraction, mEndColor, mStartColor) as Int
            var dotRadius: Float
            //是否按比例缩放点
            dotRadius = if (mDotMode == MODE_SCALE) {
                (fraction * mDotRadius).toInt().toFloat()
            } else {
                mDotRadius
            }
            points.add(Dot(x, y, color, dotRadius))
        }
        return points
    }

    private class Dot internal constructor(
        /**
         * x坐标
         */
        var x: Float,
        /**
         * y坐标
         */
        var y: Float,
        /**
         * 颜色
         */
        var color: Int,
        /**
         * 点的半径，可以一个点一个半径
         */
        var dotRadius: Float
    )

    companion object {
        /**
         * 普通模式
         */
        private const val MODE_NORMAL = 1
        /**
         * 缩放模式
         */
        private const val MODE_SCALE = 2
        /**
         * 总旋转角度
         */
        private const val TOTAL_ROTATION_ANGLE = 360
        /**
         * 间隔时间
         */
        private const val INTERVAL_TIME: Long = 65
        /**
         * View默认最小宽度
         */
        private const val DEFAULT_MIN_WIDTH = 70

        fun dip2px(context: Context, dipValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}