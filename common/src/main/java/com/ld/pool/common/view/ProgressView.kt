package com.ld.pool.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

import com.ld.pool.common.R

class ProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val mPaint = Paint()

    private var mProgress: Int = 0

    fun setProgress(progress: Int) {
        if (progress >= 0 && progress <= 100) {
            mProgress = progress
            invalidate()
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight
        //画底部背景
        mPaint.color = Color.LTGRAY
        canvas.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            RADIUS.toFloat(),
            RADIUS.toFloat(),
            mPaint
        )
        //画进度条
        mPaint.color = resources.getColor(R.color.colorPrimary)
        canvas.drawRoundRect(
            RectF(0f, 0f, width * (mProgress.toFloat() / 100), height.toFloat()),
            RADIUS.toFloat(),
            RADIUS.toFloat(),
            mPaint
        )
        //画文字图层
        mPaint.color = resources.getColor(R.color.colorPrimary)
        mPaint.textSize = sp2px(context, 20f).toFloat()
        mPaint.typeface = Typeface.DEFAULT_BOLD
        mPaint.textAlign = Paint.Align.CENTER
        val textBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val textCavas = Canvas(textBitmap)
        val content = "$mProgress%"
        val textY =
            height / 2.0f - (mPaint.fontMetricsInt.descent / 2.0f + mPaint.fontMetricsInt.ascent / 2.0f)
        textCavas.drawText(content, width / 2.0f, textY, mPaint)
        //画最上层的白色图层，未相交时不会显示出来
        mPaint.color = Color.WHITE
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        textCavas.drawRoundRect(
            RectF(
                0f,
                0f,
                width * (mProgress.toFloat() / 100),
                height.toFloat()
            ), RADIUS.toFloat(), RADIUS.toFloat(), mPaint
        )
        //画结合后的图层
        canvas.drawBitmap(textBitmap, 0f, 0f, mPaint)
        mPaint.xfermode = null
        textBitmap.recycle()
    }

    companion object {
        /**
         * 圆角弧度
         */
        private val RADIUS = 60

        fun sp2px(context: Context, spValue: Float): Int {
            val fontScale = context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
    }
}
