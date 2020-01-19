package com.ld.pool.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.ld.pool.common.R
import kotlin.math.cos
import kotlin.math.sin


open class StarsView(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) :
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
     * 外边大圆的半径
     */
    private var mOutCircleRadius = 0f
    /**
     * 里面小圆的的半径
     */
    private var mInnerCircleRadius = 0f
    /**
     * 画笔
     */
    private var mPaint: Paint? = null
    /**
     * 多少个角的五角星
     */
    private var mAngleNum = 0
    /**
     * 星星的路径
     */
    private var mPath: Path? = null
    /**
     * 星星的颜色
     */
    private var mColor = 0
    /**
     * 边的线宽
     */
    private var mEdgeLineWidth = 0f
    /**
     * 填充风格
     */
    private var mStyle = 0

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun init(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        initAttr(context, attrs, defStyleAttr)
        //取消硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        //画笔
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        if (mStyle == STYLE_FILL) {
            mPaint!!.style = Paint.Style.FILL
        } else if (mStyle == STYLE_STROKE) {
            mPaint!!.style = Paint.Style.STROKE
        }
        mPaint!!.color = mColor
        mPaint!!.strokeWidth = mEdgeLineWidth
    }

    private fun initAttr(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val defaultColor: Int = Color.argb(255, 0, 0, 0)
        val defaultNum = 5
        val mineNum = 2
        val defaultEdgeLineWidth = dip2px(context, 1f).toFloat()
        val defaultStyle = STYLE_STROKE
        if (attrs != null) {
            val array: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.StarsView, defStyleAttr, 0)
            mColor = array.getColor(R.styleable.StarsView_stv_color, defaultColor)
            val num = array.getInt(R.styleable.StarsView_stv_num, defaultNum)
            mAngleNum = if (num <= mineNum) mineNum else num
            mEdgeLineWidth =
                array.getDimension(R.styleable.StarsView_stv_edge_line_width, defaultEdgeLineWidth)
            mStyle = array.getInt(R.styleable.StarsView_stv_style, defaultStyle)
            array.recycle()
        } else {
            mColor = defaultColor
            mAngleNum = defaultNum
            mEdgeLineWidth = defaultEdgeLineWidth
            mStyle = defaultStyle
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算外边大圆的半径
        mOutCircleRadius = mViewWidth.coerceAtMost(mViewHeight) / 2f * 0.95f
        //计算里面小圆的的半径
        mInnerCircleRadius = mViewWidth.coerceAtMost(mViewHeight) / 2f * 0.5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将画布中心移动到中心点
        canvas.translate((mViewWidth / 2).toFloat(), (mViewHeight / 2).toFloat())
        //画星星
        drawStars(canvas)
    }

    /**
     * 画星星
     */
    private fun drawStars(canvas: Canvas) { //计算平均角度，例如360度分5份，每一份角都为72度
        val averageAngle = 360f / mAngleNum
        //计算大圆的外角的角度，从右上角为例计算，90度的角减去一份角，得出剩余的小角的角度，例如90 - 72 = 18 度
        val outCircleAngle = 90 - averageAngle
        //一份平均角度的一半，例如72 / 2 = 36度
        val halfAverageAngle = averageAngle / 2f
        //计算出小圆内角的角度，36 + 18 = 54 度
        val internalAngle = halfAverageAngle + outCircleAngle
        //创建2个点
        val outCirclePoint = Point()
        val innerCirclePoint = Point()
        if (mPath == null) {
            mPath = Path()
        }
        mPath!!.reset()
        for (i in 0 until mAngleNum) { //计算大圆上的点坐标
            //x = Math.cos((18 + 72 * i) / 180f * Math.PI) * 大圆半径
            //y = -Math.sin((18 + 72 * i)/ 180f * Math.PI) * 大圆半径
            outCirclePoint.x =
                (cos(angleToRadian(outCircleAngle + i * averageAngle)) * mOutCircleRadius).toInt()
            outCirclePoint.y =
                (-(sin(angleToRadian(outCircleAngle + i * averageAngle)) * mOutCircleRadius)).toInt()
            //计算小圆上的点坐标
            //x = Math.cos((54 + 72 * i) / 180f * Math.PI ) * 小圆半径
            //y = -Math.sin((54 + 72 * i) / 180 * Math.PI ) * 小圆半径
            innerCirclePoint.x =
                (cos(angleToRadian(internalAngle + i * averageAngle)) * mInnerCircleRadius).toInt()
            innerCirclePoint.y =
                (-(sin(angleToRadian(internalAngle + i * averageAngle)) * mInnerCircleRadius)).toInt()
            //第一次，先移动到第一个大圆上的点
            if (i == 0) {
                mPath!!.moveTo(outCirclePoint.x.toFloat(), outCirclePoint.y.toFloat())
            }
            //坐标连接，先大圆角上的点，再到小圆角上的点
            mPath!!.lineTo(outCirclePoint.x.toFloat(), outCirclePoint.y.toFloat())
            mPath!!.lineTo(innerCirclePoint.x.toFloat(), innerCirclePoint.y.toFloat())
        }
        mPath!!.close()
        canvas.drawPath(mPath!!, mPaint!!)
    }

    /**
     * 角度转弧度，由于Math的三角函数需要传入弧度制，而不是角度值，所以要角度换算为弧度，角度 / 180 * π
     *
     * @param angle 角度
     * @return 弧度
     */
    private fun angleToRadian(angle: Float): Double {
        return angle / 180f * Math.PI
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
         * 风格，填满
         */
        private const val STYLE_FILL = 1
        /**
         * 风格，描边
         */
        private const val STYLE_STROKE = 2

        fun dip2px(context: Context, dipValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}