package com.ld.pool.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec
import androidx.annotation.Nullable
import androidx.core.view.ViewCompat.setLayerType
import com.ld.pool.common.R


open class HookCheckBox(
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
     * 原型半径
     */
    private var mRadius = 0f
    /**
     * 画笔
     */
    private var mPaint: Paint? = null
    /**
     * 钩子的线长度
     */
    private var mHookLineLength = 0f
    /**
     * 是否选中
     */
    var isCheck = false
        private set
    /**
     * 选中时，圆的颜色
     */
    private var mCheckCircleColor = 0
    /**
     * 未选中时，圆的颜色
     */
    private var mUncheckCircleColor = 0
    /**
     * 选中时，钩子的颜色
     */
    private var mCheckHookColor = 0
    /**
     * 未选中时，钩子的颜色
     */
    private var mUncheckHookColor = 0
    /**
     * 混合模式
     */
    private var mPorterDuffXfermode: PorterDuffXfermode? = null
    /**
     * 线宽
     */
    private var mLineWidth = 0f
    /**
     * 风格策略
     */
    private var mStyleStrategy: BaseStyleStrategy? = null
    /**
     * 切换改变回调
     */
    private var mCheckListener: OnCheckChangeListener? = null

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun init(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        initAttr(context, attrs, defStyleAttr)
        //画笔
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mUncheckCircleColor
        mPaint!!.strokeWidth = mLineWidth
        mPaint!!.strokeJoin = Paint.Join.ROUND
        mPaint!!.strokeCap = Paint.Cap.ROUND
        //View禁用掉GPU硬件加速，切换到软件渲染模式
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        //设置点击事件
        setOnClickListener { }
    }

    private fun initAttr(
        context: Context, @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) { //默认的选中颜色
        val defaultCheckCircleColor: Int = Color.argb(255, 254, 201, 77)
        //默认的未选中颜色
        val defaultUncheckCircleColor: Int = Color.argb(255, 234, 234, 234)
        //默认选中的钩子颜色
        val defaultCheckHookColor: Int = Color.argb(255, 53, 40, 33)
        //默认未选中的钩子颜色
        val defaultUncheckHookColor: Int = Color.argb(255, 255, 255, 255)
        //默认风格
        val defaultStyle = STYLE_NORMAL
        //线宽
        val defaultLineWidth = dip2px(context, 1.5f).toFloat()
        //风格
        val style: Int
        if (attrs != null) {
            val array: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.HookCheckBox, defStyleAttr, 0)
            mCheckCircleColor = array.getColor(
                R.styleable.HookCheckBox_hcb_check_circle_color,
                defaultCheckCircleColor
            )
            mUncheckCircleColor = array.getColor(
                R.styleable.HookCheckBox_hcb_uncheck_circle_color,
                defaultUncheckCircleColor
            )
            mCheckHookColor =
                array.getColor(R.styleable.HookCheckBox_hcb_check_hook_color, defaultCheckHookColor)
            mUncheckHookColor = array.getColor(
                R.styleable.HookCheckBox_hcb_uncheck_hook_color,
                defaultUncheckHookColor
            )
            style = array.getInt(R.styleable.HookCheckBox_hcb_style, defaultStyle)
            isCheck = array.getBoolean(R.styleable.HookCheckBox_hcb_is_check, false)
            mLineWidth =
                array.getDimension(R.styleable.HookCheckBox_hcb_line_width, defaultLineWidth)
            array.recycle()
        } else {
            mCheckCircleColor = defaultCheckCircleColor
            mUncheckCircleColor = defaultUncheckCircleColor
            mCheckHookColor = defaultCheckHookColor
            mUncheckHookColor = defaultUncheckHookColor
            style = defaultStyle
            mLineWidth = defaultLineWidth
            isCheck = false
        }
        if (style == STYLE_HOLLOW_OUT) {
            mStyleStrategy = HollowOutStyleStrategy()
        } else if (style == STYLE_NORMAL) {
            mStyleStrategy = NormalStyleStrategy()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
        //计算圆的半径
        mRadius = mViewWidth.coerceAtMost(mViewHeight) / 2f * 0.90f
        //计算对勾的长度
        mHookLineLength = mViewWidth.coerceAtMost(mViewHeight) / 2f * 0.8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = -mViewWidth / 2f
        val top = -mViewHeight / 2f
        //保存图层
        val layerId: Int = canvas.saveLayer(
            left,
            top,
            width.toFloat(),
            height.toFloat(),
            null,
            Canvas.ALL_SAVE_FLAG
        )
        //将画布中心移动到中心点
        canvas.translate((mViewWidth / 2).toFloat(), (mViewHeight / 2).toFloat())
        //画圆形背景
        mStyleStrategy!!.drawCircleBg(canvas)
        //画钩子
        mStyleStrategy!!.drawHook(canvas)
        //恢复图层
        canvas.restoreToCount(layerId)
    }

    private open inner class BaseStyleStrategy {
        /**
         * 画圆形背景
         */
        open fun drawCircleBg(canvas: Canvas) { //设置背景圆的颜色
            if (isCheck) {
                mPaint?.color = mCheckCircleColor
            } else {
                mPaint?.color = mUncheckCircleColor
            }
            canvas.drawCircle(0F, 0F, mRadius, mPaint!!)
        }

        /**
         * 画钩子
         */
        open fun drawHook(canvas: Canvas) { //设置钩子的颜色
            if (isCheck) {
                mPaint?.color = mCheckHookColor
            } else {
                mPaint?.color = mUncheckHookColor
            }
            //画钩子要用描边风格
            mPaint?.style = Paint.Style.STROKE
            canvas.save()
            //画布向下平移一半的半径长度
            canvas.translate(-(mRadius / 8f), mRadius / 3f)
            //旋转画布45度
            canvas.rotate((-45).toFloat())
            val path = Path()
            path.reset()
            path.moveTo(0F, 0F)
            //向右画一条线
            path.lineTo(mHookLineLength, 0F)
            //回到中心点
            path.moveTo(0F, 0F)
            //向上画一条线
            path.lineTo(0F, -mHookLineLength / 2f)
            //画路径
            canvas.drawPath(path, mPaint!!)
            canvas.restore()
        }
    }

    private inner class NormalStyleStrategy : BaseStyleStrategy() {
        override fun drawCircleBg(canvas: Canvas) { //普通风格用填充风格
            mPaint?.style = Paint.Style.FILL
            super.drawCircleBg(canvas)
        }
    }

    /**
     * 镂空风格
     */
    private inner class HollowOutStyleStrategy : BaseStyleStrategy() {
        override fun drawCircleBg(canvas: Canvas) {
            if (isCheck) { //镂空风格，选中时用填充
                mPaint?.style = Paint.Style.FILL
            } else { //镂空风格，未选中时用描边
                mPaint?.style = Paint.Style.STROKE
            }
            super.drawCircleBg(canvas)
        }

        override fun drawHook(canvas: Canvas) { //镂空风格，选中时，才画钩子
            if (isCheck) { //设置混合模式
                mPaint?.xfermode = mPorterDuffXfermode
                super.drawHook(canvas)
                //去除混合模式
                mPaint?.xfermode = null
            }
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

    override fun setOnClickListener(@Nullable l: OnClickListener?) {
        super.setOnClickListener(OnClickWrapper(l))
    }

    /**
     * 点击事件包裹，避免外部设置点击事件将内部的切换事件替换
     */
    private inner class OnClickWrapper : OnClickListener {
        private var mOriginListener: OnClickListener? = null

        internal constructor() {}
        internal constructor(originListener: OnClickListener?) {
            mOriginListener = originListener
        }

        override fun onClick(view: View?) {
            isCheck = !isCheck
            invalidate()
            if (mCheckListener != null) {
                mCheckListener!!.onCheckChange(isCheck)
            }
            if (mOriginListener != null) {
                mOriginListener!!.onClick(view)
            }
        }
    }

    fun setCheck(check: Boolean): HookCheckBox {
        isCheck = check
        if (mCheckListener != null) {
            mCheckListener!!.onCheckChange(check)
        }
        return this
    }

    interface OnCheckChangeListener {
        /**
         * 切换时回调
         *
         * @param isCheck 是否选中
         */
        fun onCheckChange(isCheck: Boolean)
    }

    fun setOnCheckChangeListener(listener: OnCheckChangeListener?) {
        mCheckListener = listener
    }

    companion object {
        /**
         * View默认最小宽度
         */
        private const val DEFAULT_MIN_WIDTH = 80
        /**
         * 普通风格
         */
        private const val STYLE_NORMAL = 1
        /**
         * 镂空风格
         */
        private const val STYLE_HOLLOW_OUT = 2

        fun dip2px(context: Context, dipValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}