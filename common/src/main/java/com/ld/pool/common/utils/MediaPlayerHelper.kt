package com.ld.pool.common.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi


/**
 * 多媒体播放
 */
class MediaPlayerHelper {
    private val ext = arrayOf(
        ".3gp",
        ".3GP",
        ".mp4",
        ".MP4",
        ".mp3",
        ".ogg",
        ".OGG",
        ".MP3",
        ".wav",
        ".WAV"
    ) //定义我们支持的文件格式
    private var uiHolder: Holder? = null//UI的容器
    private var delaySecondTime = 1000 //进度回调间隔
    private var isHolderCreate = false //SurfaceHolder是否准备好了

    /** 状态枚举  */
    enum class CallBackState(private val state: String) {
        PREPARE("MediaPlayer--准备完毕"), COMPLETE("MediaPlayer--播放结束"), ERROR("MediaPlayer--播放错误"), EXCEPTION(
            "MediaPlayer--播放异常"
        ),
        INFO("MediaPlayer--播放开始"), PROGRESS("MediaPlayer--播放进度回调"), SEEK_COMPLETE("MediaPlayer--拖动到尾端"), VIDEO_SIZE_CHANGE(
            "MediaPlayer--读取视频大小"
        ),
        BUFFER_UPDATE("MediaPlayer--更新流媒体缓存状态"), FORMATE_NOT_SURPORT("MediaPlayer--音视频格式可能不支持"), SURFACEVIEW_NULL(
            "SurfaceView--还没初始化"
        ),
        SURFACEVIEW_NOT_ARREADY("SurfaceView--还没准备好"), SURFACEVIEW_CHANGE("SurfaceView--Holder改变"), SURFACEVIEW_CREATE(
            "SurfaceView--Holder创建"
        ),
        SURFACEVIEW_DESTROY("SurfaceView--Holder销毁");

        override fun toString(): String {
            return state
        }

    }

    /**
     * 获得流媒体对象
     * @return 对象
     */
    val mediaPlayer: MediaPlayer?
        get() = uiHolder?.player

    /**
     * 设置播放进度时间间隔
     * @param time 时间
     * @return 类对象
     */
    fun setProgressInterval(time: Int): MediaPlayerHelper? {
        delaySecondTime = time
        return instance
    }

    /**
     * 通过Assets文件名播放Assets目录下的音频
     * @param context 引用
     * @param assetName 名字,带后缀，比如:text.mp3
     */
    fun playAsset(
        context: Context,
        assetName: String,
        isVideo: Boolean
    ) {
        if (!checkAvalable(assetName)) {
            onStatusCallbackNext(CallBackState.FORMATE_NOT_SURPORT, assetName)
            return
        }
        if (isVideo) {
            if (isHolderCreate) {
                beginPlayAsset(context, assetName)
            } else {
                setOnHolderCreateListener(object : OnHolderCreateListener {
                    override fun onHolderCreate() {
                        beginPlayAsset(
                            context,
                            assetName
                        )
                    }
                })
            }
        } else {
            beginPlayAsset(context, assetName)
        }
    }

    /**
     * 通过文件路径播放音视频
     * @param path 路径
     */
    fun playUrl(context: Context, path: String, isVideo: Boolean) {
        if (isVideo) {
            if (isHolderCreate) {
                beginPlayUrl(context, path)
            } else {
                setOnHolderCreateListener(object : OnHolderCreateListener {
                    override fun onHolderCreate() {
                        beginPlayUrl(context, path)
                    }
                })
            }
        } else {
            beginPlayUrl(context, path)
        }
    }

    /**
     * 播放流视频
     * @param videoBuffer videoBuffer
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun playByte(videoBuffer: ByteArray?, isVideo: Boolean) {
        if (isVideo) {
            if (isHolderCreate) {
                beginPlayDataSource(ByteMediaDataSource(videoBuffer!!))
            } else {
                setOnHolderCreateListener(object : OnHolderCreateListener {
                    override fun onHolderCreate() {
                        beginPlayDataSource(
                            ByteMediaDataSource(videoBuffer!!)
                        )
                    }
                })
            }
        } else {
            beginPlayDataSource(ByteMediaDataSource(videoBuffer!!))
        }
    }

    /**
     * 停止资源
     */
    fun stop() {
        if (uiHolder?.player != null) {
            uiHolder?.player!!.stop()
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        if (uiHolder?.player != null) {
            uiHolder?.player!!.release()
            uiHolder?.player = null
        }
        refress_time_handler.removeCallbacks(refress_time_Thread)
    }

    /**
     * 重新创建MediaPlayer
     */
    fun reCreateMediaPlayer() {
        if (uiHolder?.player != null) {
            if (uiHolder?.player!!.isPlaying) {
                uiHolder?.player!!.stop()
            }
            uiHolder?.player!!.release()
            uiHolder?.player = MediaPlayer()
        } else {
            uiHolder?.player = MediaPlayer()
        }
        initPlayerListener()
    }

    /**
     * 设置SurfaceView
     * @param surfaceView 控件
     * @return 类对象
     */
    fun setSurfaceView(surfaceView: SurfaceView?): MediaPlayerHelper? {
        if (surfaceView == null) {
            onStatusCallbackNext(CallBackState.SURFACEVIEW_NULL, uiHolder?.player!!)
        } else {
            uiHolder?.surfaceView = surfaceView
            uiHolder?.surfaceHolder = uiHolder?.surfaceView!!.holder
            uiHolder?.surfaceHolder!!.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    isHolderCreate = true
                    if (uiHolder?.player != null && holder != null) { //解决部分机型/电视播放的时候有声音没画面的情况
                        if (uiHolder?.surfaceView != null) {
                            uiHolder?.surfaceView!!.post(Runnable {
                                holder.setFixedSize(
                                    uiHolder?.surfaceView!!.width,
                                    uiHolder?.surfaceView!!.height
                                )
                                uiHolder?.player!!.setDisplay(holder)
                            })
                        }
                    }
                    onStatusCallbackNext(CallBackState.SURFACEVIEW_CREATE, holder)
                    onHolderCreateNext()
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                    onStatusCallbackNext(CallBackState.SURFACEVIEW_CHANGE, format, width, height)
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    isHolderCreate = false
                    onStatusCallbackNext(CallBackState.SURFACEVIEW_DESTROY, holder)
                }
            })
        }
        return instance
    }

    /**
     * 时间监听
     */
    private fun initPlayerListener() {
        uiHolder?.player!!.setOnCompletionListener { mp: MediaPlayer? ->
            onStatusCallbackNext(CallBackState.PROGRESS, 100)
            onStatusCallbackNext(CallBackState.COMPLETE, mp!!)
        }
        uiHolder?.player!!.setOnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
            val errorString = "what:$what extra:$extra"
            onStatusCallbackNext(CallBackState.ERROR, errorString)
            false
        }
        uiHolder?.player!!.setOnInfoListener { mp: MediaPlayer?, what: Int, extra: Int ->
            onStatusCallbackNext(CallBackState.INFO, mp!!, what, extra)
            false
        }
        uiHolder?.player!!.setOnPreparedListener { mp: MediaPlayer? ->
            try {
                if (uiHolder?.surfaceView != null) { //解决部分机型/电视播放的时候有声音没画面的情况
                    uiHolder?.surfaceView!!.post(Runnable {
                        uiHolder?.surfaceHolder!!.setFixedSize(
                            uiHolder?.surfaceView!!.width,
                            uiHolder?.surfaceView!!.height
                        )
                        //设置预览区域
                        uiHolder?.player!!.setDisplay(uiHolder?.surfaceHolder)
                    })
                }
                uiHolder?.player!!.start()
                refress_time_handler.postDelayed(refress_time_Thread, delaySecondTime.toLong())
            } catch (e: Exception) {
                onStatusCallbackNext(CallBackState.EXCEPTION, e.toString())
            }
            var holderMsg = "holder -"
            if (uiHolder?.surfaceHolder != null) {
                holderMsg =
                    holderMsg + " height：" + uiHolder?.surfaceHolder!!.surfaceFrame.height()
                holderMsg = holderMsg + " width：" + uiHolder?.surfaceHolder!!.surfaceFrame.width()
            }
            onStatusCallbackNext(CallBackState.PREPARE, holderMsg)
        }
        uiHolder?.player!!.setOnSeekCompleteListener { mp: MediaPlayer? ->
            onStatusCallbackNext(
                CallBackState.SEEK_COMPLETE,
                mp!!
            )
        }
        uiHolder?.player!!.setOnVideoSizeChangedListener { mp: MediaPlayer?, width: Int, height: Int ->
            onStatusCallbackNext(
                CallBackState.VIDEO_SIZE_CHANGE,
                width,
                height
            )
        }
        uiHolder?.player!!.setOnBufferingUpdateListener { mp: MediaPlayer?, percent: Int ->
            onStatusCallbackNext(
                CallBackState.BUFFER_UPDATE,
                mp!!,
                percent
            )
        }
    }

    /**
     * 播放
     * @param path 参数
     */
    private fun beginPlayUrl(context: Context, path: String) { /*
         * 其实仔细观察优酷app切换播放网络视频时的确像是这样做的：先暂停当前视频，
         * 让mediaplayer与先前的surfaceHolder脱离“绑定”,当mediaplayer再次准备好要start时，
         * 再次让mediaplayer与surfaceHolder“绑定”在一起，显示下一个要播放的视频。
         * 注：MediaPlayer.setDisplay()的作用： 设置SurfaceHolder用于显示的视频部分媒体。
         */
        try { //Uri url = Uri.fromFile(new File(path));
            uiHolder?.player!!.setDisplay(null)
            uiHolder?.player!!.reset()
            uiHolder?.player!!.setDataSource(path)
            uiHolder?.player!!.prepareAsync()
        } catch (e: Exception) {
            onStatusCallbackNext(CallBackState.ERROR, e.toString())
        }
    }

    /**
     * 播放
     * @param assetName 参数
     */
    private fun beginPlayAsset(context: Context, assetName: String) { /*
         * 其实仔细观察优酷app切换播放网络视频时的确像是这样做的：先暂停当前视频，
         * 让mediaplayer与先前的surfaceHolder脱离“绑定”,当mediaplayer再次准备好要start时，
         * 再次让mediaplayer与surfaceHolder“绑定”在一起，显示下一个要播放的视频。
         * 注：MediaPlayer.setDisplay()的作用： 设置SurfaceHolder用于显示的视频部分媒体。
         */
        val assetMg: AssetManager = context.getAssets()
        try {
            uiHolder?.assetDescriptor = assetMg.openFd(assetName)
            uiHolder?.player!!.setDisplay(null)
            uiHolder?.player!!.reset()
            uiHolder?.player!!.setDataSource(
                uiHolder?.assetDescriptor!!.fileDescriptor,
                uiHolder?.assetDescriptor!!.startOffset,
                uiHolder?.assetDescriptor!!.length
            )
            uiHolder?.player!!.prepareAsync()
        } catch (e: Exception) {
            onStatusCallbackNext(CallBackState.ERROR, e.toString())
        }
    }

    /**
     * 播放
     * @param mediaDataSource 参数
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun beginPlayDataSource(mediaDataSource: MediaDataSource) { /*
         * 其实仔细观察优酷app切换播放网络视频时的确像是这样做的：先暂停当前视频，
         * 让mediaplayer与先前的surfaceHolder脱离“绑定”,当mediaplayer再次准备好要start时，
         * 再次让mediaplayer与surfaceHolder“绑定”在一起，显示下一个要播放的视频。
         * 注：MediaPlayer.setDisplay()的作用： 设置SurfaceHolder用于显示的视频部分媒体。
         */
        try {
            uiHolder?.player!!.setDisplay(null)
            uiHolder?.player!!.reset()
            uiHolder?.player!!.setDataSource(mediaDataSource)
            uiHolder?.player!!.prepareAsync()
        } catch (e: Exception) {
            onStatusCallbackNext(CallBackState.ERROR, e.toString())
        }
    }

    /**
     * 检查是否可以播放
     * @param path 参数
     * @return 结果
     */
    private fun checkAvalable(path: String): Boolean {
        var surport = false
        for (s in ext) {
            if (path.endsWith(s)) {
                surport = true
            }
        }
        if (!surport) {
            onStatusCallbackNext(CallBackState.FORMATE_NOT_SURPORT, uiHolder?.player!!)
            return false
        }
        return true
    }

    /**
     * 播放进度定时器
     */
    private val refress_time_handler: Handler = Handler()
    private val refress_time_Thread: Runnable = object : Runnable {
        override fun run() {
            refress_time_handler.removeCallbacks(this)
            try {
                if (uiHolder?.player != null && uiHolder?.player!!.isPlaying) {
                    val duraction = uiHolder?.player!!.duration
                    if (duraction > 0) {
                        onStatusCallbackNext(
                            CallBackState.PROGRESS,
                            100 * uiHolder?.player!!.currentPosition / duraction
                        )
                    }
                }
            } catch (e: IllegalStateException) {
                onStatusCallbackNext(CallBackState.EXCEPTION, e.toString())
            }
            refress_time_handler.postDelayed(this, delaySecondTime.toLong())
        }
    }

    /* ***************************** Holder封装UI ***************************** */
    private class Holder {
        var surfaceHolder: SurfaceHolder? = null
        var player: MediaPlayer? = null
        var surfaceView: SurfaceView? = null
        var assetDescriptor: AssetFileDescriptor? = null
    }

    /* ***************************** StatusCallback ***************************** */
    private var onStatusCallbackListener: OnStatusCallbackListener? = null

    // 接口类 -> OnStatusCallbackListener
    interface OnStatusCallbackListener {
        fun onStatusonStatusCallbackNext(
            status: CallBackState?,
            vararg args: Any?
        )
    }

    // 对外暴露接口 -> setOnStatusCallbackListener
    fun setOnStatusCallbackListener(onStatusCallbackListener: OnStatusCallbackListener?): MediaPlayerHelper? {
        this.onStatusCallbackListener = onStatusCallbackListener
        return instance
    }

    // 内部使用方法 -> StatusCallbackNext
    private fun onStatusCallbackNext(status: CallBackState, vararg args: Any) {
        if (onStatusCallbackListener != null) {
            onStatusCallbackListener!!.onStatusonStatusCallbackNext(status, *args)
        }
    }

    /* ***************************** HolderCreate(内部使用) ***************************** */
    private var onHolderCreateListener: OnHolderCreateListener? = null

    // 接口类 -> OnHolderCreateListener
    private interface OnHolderCreateListener {
        fun onHolderCreate()
    }

    // 内部露接口 -> setOnHolderCreateListener
    private fun setOnHolderCreateListener(onHolderCreateListener: OnHolderCreateListener) {
        this.onHolderCreateListener = onHolderCreateListener
    }

    // 内部使用方法 -> HolderCreateNext
    private fun onHolderCreateNext() {
        if (onHolderCreateListener != null) {
            onHolderCreateListener!!.onHolderCreate()
        }
    }

    companion object {
        private var instance: MediaPlayerHelper? = null
        /**
         * 获得静态类
         * @return 类对象
         */
        @Synchronized
        fun getInstance(): MediaPlayerHelper? {
            if (instance == null) {
                instance = MediaPlayerHelper()
            }
            return instance
        }
    }

    /**
     * 构造函数
     */
    init {
        if (instance == null) {
            instance = this
        }
        uiHolder = Holder()
        uiHolder!!.player = MediaPlayer()
        initPlayerListener()
    }
}