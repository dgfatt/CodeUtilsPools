package com.ld.pool.common.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.OnDoubleTapListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.Scroller
import androidx.annotation.Nullable
import java.io.IOException
import java.io.InputStream


/**
 * @author chs
 * date：2019-11-08 16:01
 * des： 加载大图
 */
open class MyBigView : View, GestureDetector.OnGestureListener, OnDoubleTapListener,
    OnScaleGestureListener {
    /**
     * 图片的宽和高
     */
    private var mImageWidth = 0f
    private var mImageHeight = 0f
    /**
     * 当前View的宽和高
     */
    private var mViewWidth = 0f
    private var mViewHeight = 0f
    /**
     * 图片的缩放比
     */
    private var mScale = 1f
    private var mCurrentScale = 1f
    /**
     * 放大几倍
     */
    private val mMultiple = 3
    /**
     * 绘制区域
     */
    private val mRect: Rect = Rect()
    /**
     * 分区域加载器
     */
    private var mRegionDecoder: BitmapRegionDecoder? = null
    private var mOptions: BitmapFactory.Options? = null
    private var mBitmap: Bitmap? = null
    /**
     * 滑动器
     */
    private var mScroller: Scroller? = null
    /**
     * 缩放矩阵
     */
    private var mMatrix: Matrix? = null
    /**
     * 手势识别器
     */
    private var mGestureDetector: GestureDetector? = null
    private var mScaleGestureDetector: ScaleGestureDetector? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        mOptions = BitmapFactory.Options()
        //滑动器
        mScroller = Scroller(getContext())
        //所放器
        mMatrix = Matrix()
        //手势识别
        mGestureDetector = GestureDetector(getContext(), this)
        mScaleGestureDetector = ScaleGestureDetector(getContext(), this)
    }

    fun setImage(`is`: InputStream?) {
        mOptions!!.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, mOptions)
        mImageWidth = mOptions!!.outWidth.toFloat()
        mImageHeight = mOptions!!.outHeight.toFloat()
        mOptions!!.inPreferredConfig = Bitmap.Config.RGB_565
        mOptions!!.inJustDecodeBounds = false
        try { //区域解码器
            mRegionDecoder = BitmapRegionDecoder.newInstance(`is`, false)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        requestLayout()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w.toFloat()
        mViewHeight = h.toFloat()
        mRect.top = 0
        mRect.left = 0
        mRect.right = mViewWidth.toInt()
        mRect.bottom = mViewHeight.toInt()
        mScale = mViewWidth / mImageWidth
        mCurrentScale = mScale
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mRegionDecoder == null) {
            return
        }
        //复用内存
        mOptions!!.inBitmap = mBitmap
        mBitmap = mRegionDecoder!!.decodeRegion(mRect, mOptions)
        mMatrix?.setScale(mCurrentScale, mCurrentScale)
        canvas.drawBitmap(mBitmap!!, mMatrix!!, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector!!.onTouchEvent(event)
        mScaleGestureDetector!!.onTouchEvent(event)
        return true
    }

    override fun onDown(e: MotionEvent): Boolean { //如果正在滑动，先停止
        if (!mScroller!!.isFinished) {
            mScroller!!.forceFinished(true)
        }
        return true
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean { //滑动的时候，改变mRect显示区域的位置
        mRect.offset(distanceX.toInt(), distanceY.toInt())
        //处理上下左右的边界
        handleBorder()
        invalidate()
        return false
    }

    private fun handleBorder() {
        if (mRect.left < 0) {
            mRect.left = 0
            mRect.right = (mViewWidth / mCurrentScale).toInt()
        }
        if (mRect.right > mImageWidth) {
            mRect.right = mImageWidth.toInt()
            mRect.left = (mImageWidth - mViewWidth / mCurrentScale).toInt()
        }
        if (mRect.top < 0) {
            mRect.top = 0
            mRect.bottom = (mViewHeight / mCurrentScale).toInt()
        }
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight.toInt()
            mRect.top = (mImageHeight - mViewHeight / mCurrentScale).toInt()
        }
    }

    override fun onLongPress(e: MotionEvent) {}

    override fun computeScroll() {
        super.computeScroll()
        if (!mScroller!!.isFinished && mScroller!!.computeScrollOffset()) {
            if (mRect.top + mViewHeight / mCurrentScale < mImageHeight) {
                mRect.top = mScroller!!.currY
                mRect.bottom = ((mRect.top + mViewHeight / mCurrentScale).toInt())
            }
            if (mRect.bottom > mImageHeight) {
                mRect.top = (mImageHeight - mViewHeight / mCurrentScale).toInt()
                mRect.bottom = mImageHeight.toInt()
            }
            invalidate()
        }
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        mScroller!!.fling(
            mRect.left,
            mRect.top,
            (-velocityX).toInt(),
            (-velocityY).toInt(),
            0,
            mImageWidth.toInt()
            ,
            0,
            mImageHeight.toInt()
        )
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(e: MotionEvent): Boolean { //处理双击事件
        mCurrentScale = if (mCurrentScale > mScale) {
            mScale
        } else {
            mScale * mMultiple
        }
        mRect.right = mRect.left + (mViewWidth / mCurrentScale).toInt()
        mRect.bottom = mRect.top + (mViewHeight / mCurrentScale).toInt()
        //处理上下左右的边界
        handleBorder()
        invalidate()
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean { //处理手指缩放事件
//获取与上次事件相比，得到的比例因子
        val scaleFactor = detector.scaleFactor
        //        mCurrentScale+=scaleFactor-1;
        mCurrentScale *= scaleFactor
        if (mCurrentScale > mScale * mMultiple) {
            mCurrentScale = mScale * mMultiple
        } else if (mCurrentScale <= mScale) {
            mCurrentScale = mScale
        }
        mRect.right = mRect.left + (mViewWidth / mCurrentScale).toInt()
        mRect.bottom = mRect.top + (mViewHeight / mCurrentScale).toInt()
        invalidate()
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean { //当 >= 2 个手指碰触屏幕时调用，若返回 false 则忽略改事件调用
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {}
}