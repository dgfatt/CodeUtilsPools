package com.ld.pool.common.utils;

import android.media.MediaRecorder;
import android.os.Environment;

import com.ld.pool.common.listener.VideoRecord;

import java.io.File;
import java.io.IOException;

public class VideoRecordManager {

    private MediaRecorder mRecorder = null;
    private VideoRecord videoRecord;

    public VideoRecordManager(VideoRecord videoRecord) {
        this.videoRecord = videoRecord;
    }

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        videoRecord.getCamera().unlock();
        mRecorder.setCamera(videoRecord.getCamera());
        // 这两项需要放在setOutputFormat之前
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);//声音源
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源

        // 设置输出格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        // 这两项需要放在setOutputFormat之后
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//声音编码格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//视频编码格式
        //设置视频的长宽
        mRecorder.setVideoSize(640, 480);
        mRecorder.setVideoFrameRate(30);
        //设置比特率（比特率越高质量越高同样也越大）
        mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
        //这里是调整旋转角度（前置和后置的角度不一样）
        mRecorder.setOrientationHint(270);
        //设置记录会话的最大持续时间（毫秒）
        mRecorder.setMaxDuration(10 * 1000);
        //设置预览对象
        mRecorder.setPreviewDisplay(videoRecord.getSurfaceHolder().getSurface());
        //设置输出的文件路径
        mRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + File.separator + "test.mp4");
        //预处理
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开始录制
        mRecorder.start();
    }

    public void stop() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
    }
}
