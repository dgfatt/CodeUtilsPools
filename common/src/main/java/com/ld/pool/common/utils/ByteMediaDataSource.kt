package com.ld.pool.common.utils

import android.media.MediaDataSource

import android.os.Build

import androidx.annotation.RequiresApi


/**
 * MediaPlayer播放字节流的工具类，可用于视频加密解密播放方案
 * Created by haide.yin(haide.yin@tcl.com) on 2019/12/13 8:56.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
class ByteMediaDataSource(private val videoBuffer: ByteArray) : MediaDataSource() {

    override fun readAt(
        position: Long,
        buffer: ByteArray,
        offset: Int,
        size: Int
    ): Int {
        var size = size
        synchronized(videoBuffer) {
            val length = videoBuffer.size
            if (position >= length) {
                return -1 // -1 indicates EOF
            }
            if (position + size > length) {
                size -= (position + size).toInt() - length
            }
            System.arraycopy(videoBuffer, position.toInt(), buffer, offset, size)
            return size
        }
    }

    override fun getSize(): Long {
        synchronized(videoBuffer) { return videoBuffer.size.toLong() }
    }

    override fun close() {}

}