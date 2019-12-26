package com.ld.pool.common.base

import android.app.Application
import com.blankj.utilcode.util.Utils

class BaseApplication : Application() {

    init {
        Utils.init(this)
    }
}