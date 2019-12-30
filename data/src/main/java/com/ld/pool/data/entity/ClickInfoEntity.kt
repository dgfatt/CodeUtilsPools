package com.ld.pool.data.entity

import android.view.View

/**
 * 点击模型 保存id 以及坐标信息
 * 用于处理整个Item中的点击事件分发
 */
class ClickInfoEntity {
    var mBottom: Int
    var mGroupId: Int = View.NO_ID
    /**
     * 详细信息
     * 对应的子view坐标信息
     */
    var mDetailInfoList: MutableList<DetailInfo>? = null

    constructor(bottom: Int) {
        mBottom = bottom
    }

    constructor(bottom: Int, detailInfoList: MutableList<DetailInfo>?) {
        mBottom = bottom
        mDetailInfoList = detailInfoList
    }

    class DetailInfo(var id: Int, var left: Int, var right: Int, var top: Int, var bottom: Int)
}