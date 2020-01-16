package com.ld.pool.codeutilspools.ui.bottomaction.ui.notifications

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.utils.MediaPlayerHelper
import com.ld.pool.common.utils.MediaPlayerHelper.*
import java.io.File
import java.io.IOException
import java.io.InputStream


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val URL =
        "https://github.com/yinhaide/HDMediaPlayer/blob/master/app/src/main/assets/demo.mp4"
    private val URL2 =
        "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"

    private var surfaceView: SurfaceView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        initView(root)
        initData()
        return root
    }

    private fun initView(root: View?) {
        surfaceView = root?.findViewById(R.id.surfaceView)
    }

    private fun initData() {
        MediaPlayerHelper
            .getInstance()              //单例
            ?.setSurfaceView(surfaceView)//设置预览区域
            ?.setProgressInterval(1000)  //设置进度回调间隔
            ?.setOnStatusCallbackListener(object : OnStatusCallbackListener {
                override fun onStatusonStatusCallbackNext(
                    status: CallBackState?,
                    vararg args: Any?
                ) {
                    LogUtils.v("--" + status.toString());
                    if (status == MediaPlayerHelper.CallBackState.PROGRESS) {
                        if (args.isNotEmpty()) {
                            ToastUtils.showLong("进度:" + args[0] + "%");
                        }
                    } else if (status == MediaPlayerHelper.CallBackState.COMPLETE) {
                        //MediaPlayerHelper.getInstance().playAsset(activity,"test.mp4");
                    } else if (status == MediaPlayerHelper.CallBackState.BUFFER_UPDATE) {
                        if (args.isNotEmpty()) {
                            ToastUtils.showLong("网络缓冲:" + args[1] + "%");
                        }
                    } else if (status == MediaPlayerHelper.CallBackState.EXCEPTION) {
                        if (args.isNotEmpty()) {
                            ToastUtils.showLong("播放异常:" + args[0]);
                        }
                    } else if (status == MediaPlayerHelper.CallBackState.ERROR) {
                        if (args.isNotEmpty()) {
                            ToastUtils.showLong("播放错误:" + args[0]);
                        }
                    }
                }
            })
            ?.playAsset(activity!!, "test.mp4", true);//开始播放
    }

    fun onNexts(`object`: Any?) {}

    private fun playassetMP3(view: View) {
        MediaPlayerHelper.getInstance()!!.playAsset(activity!!, "test.mp3", false)
    }

    private fun playAssetMP4(view: View) {
        MediaPlayerHelper.getInstance()!!.playAsset(activity!!, "test.mp4", true)
    }

    private fun playNetMP4(view: View) {
        MediaPlayerHelper.getInstance()!!.playUrl(activity!!, URL2, true)
    }

    private fun stop(view: View) {
        MediaPlayerHelper.getInstance()?.mediaPlayer?.pause()
    }

    private fun start(view: View) {
        MediaPlayerHelper.getInstance()?.mediaPlayer?.start()
    }

    private fun encry(view: View) { //写入加密字节流到指定文件
//byte[] videoBuffer = EnDecryUtil.deEncrypt("/mnt/sdcard/test.mp4");
//EnDecryUtil.writeToLocal(videoBuffer,"/mnt/sdcard/test.hd");
//        try {
//            val inputStream: InputStream = activity!!.assets.open("test.hd")
//            val videoBuffer: ByteArray = EnDecryUtil.toByteArray(inputStream)
//            inputStream.close()
//            val version = Build.VERSION.SDK_INT
//            //如果会Android6.0及以上则解密流进行播放
//            if (version >= Build.VERSION_CODES.M) { // 播放加密的视频流
//                MediaPlayerHelper.getInstance()!!.playByte(EnDecryUtil.deEncrypt(videoBuffer), true)
//            } else { //如果是Android6.0以下，则先解密然后存到本地再播放
//                //为了不让用户看到，存缓存文件为.temp，名字唯一
//                val videoPath = "/mnt/sdcard/.temp"
//                val videoFile = File(videoPath)
//                //将流解密存到本地
//                EnDecryUtil.writeToLocal(EnDecryUtil.deEncrypt(videoBuffer), videoPath)
//                if (videoFile.exists()) {
//                    MediaPlayerHelper.getInstance()!!.playUrl(activity!!, videoPath, true)
//                } else {
//                    MediaPlayerHelper.getInstance()!!.playAsset(activity!!, "test.mp4", true)
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    override fun onStop() {
        super.onStop()
        MediaPlayerHelper.getInstance()!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerHelper.getInstance()!!.release()
    }

}