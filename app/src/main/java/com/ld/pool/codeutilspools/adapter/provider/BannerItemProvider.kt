package com.ld.pool.codeutilspools.adapter.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.google.common.base.Function
import com.google.common.collect.Lists.transform
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.image.glide.GlideImageLoader
import com.ld.pool.data.bean.BannerBean
import com.ld.pool.data.bean.FloatingTabMultipleBean
import com.youth.banner.Banner


class BannerItemProvider : BaseItemProvider<FloatingTabMultipleBean, BaseViewHolder>() {

    override fun layout(): Int {
        return R.layout.item_banner
    }

    override fun viewType(): Int {
        return FloatingTabMultipleBean.ITEM_TYPE_BANNER
    }

    override fun convert(helper: BaseViewHolder, data: FloatingTabMultipleBean?, position: Int) {
        val banner = helper.getView<Banner>(R.id.banner)

        banner.isFocusableInTouchMode = false; //设置不需要焦点
        banner.requestFocus(); //设置焦点不需要

        val urls: MutableList<String> = transform(
            data!!.bannerListBean,
            Function<BannerBean, String> {
                return@Function it?.url
            })

        banner.setImages(urls)
            .setImageLoader(GlideImageLoader())
            .start()
    }

}