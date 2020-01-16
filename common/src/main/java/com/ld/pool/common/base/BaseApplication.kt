package com.ld.pool.common.base

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.ld.pool.common.BuildConfig

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initUtils()
    }

    private fun initUtils() {
        Utils.init(this)
    }

}