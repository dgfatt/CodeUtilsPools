package com.ld.pool.common.utils

import android.content.Context
import android.os.Build

import androidx.annotation.ColorRes


class ResourceUtils {

    companion object {
        fun getColor(context: Context, @ColorRes colorId: Int): Int {
            return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                context.resources.getColor(colorId)
            } else {
                context.getColor(colorId)
            }
        }
    }
}