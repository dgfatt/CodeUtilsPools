package com.ld.pool.common.listener

import android.graphics.Rect

interface IPinnedHeaderDecoration {

    val pinnedHeaderRect: Rect?

    val pinnedHeaderPosition: Int

}