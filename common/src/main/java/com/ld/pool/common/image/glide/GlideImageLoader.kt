package com.ld.pool.common.image.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ld.pool.common.R
import com.youth.banner.loader.ImageLoader


class GlideImageLoader : ImageLoader() {
    override fun displayImage(
        context: Context,
        path: Any?,
        imageView: ImageView?
    ) {
        val sharedOptions: RequestOptions = RequestOptions()
            .placeholder(R.mipmap.ic_banner_default)
            .fitCenter()

        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        imageView?.let {
            Glide.with(context.applicationContext)
                .apply { sharedOptions }
                .load(path)
                .into(it)
        }
    } //    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}