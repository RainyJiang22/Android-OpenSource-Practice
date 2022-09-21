package com.rainy.glidektx

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * @author jiangshiyu
 * @date 2022/9/21
 */
object GlideAsyncLoad {


    /**
     * Glide异步加载
     */
    fun loadAsync(glide: RequestManager, url: String, result: (bitmap: Bitmap) -> Unit) {
        glide.asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    result.invoke(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}