package com.rainy.glidektx.glide

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


/**
 * @author jiangshiyu
 * @date 2022/10/25
 */
class GlideImageLoader(
    private val mImageView: ImageView?,
    private val mProgressBar: ProgressBar?
) {


    //加载图片
    fun load(url: String?, options: RequestOptions?) {
        if (options == null) return

        onConnecting()

        DispatchingProgressManager.expect(url, object : OnProgressBarListener { //4

            override val currentPercentage: Float //5
                get() = 1.0f

            override fun onProgress(bytesRead: Long, expectedLength: Long) {
                if (mProgressBar != null) {
                    Log.d("glide", "onProgress: ${(100 * bytesRead / expectedLength).toInt()}")
                    mProgressBar.progress = (100 * bytesRead / expectedLength).toInt() //6
                }
            }
        })

        Glide.with(mImageView!!.context)
            .load(url)
            .apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    DispatchingProgressManager.forget(url)
                    onFinished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    DispatchingProgressManager.forget(url)
                    onFinished()
                    return false
                }

            })
            .into(mImageView)
    }


    //开始下载
    private fun onConnecting() {
        if (mProgressBar != null) mProgressBar.visibility = View.VISIBLE
    }

    //下载结束
    private fun onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.visibility = View.GONE
            mImageView.visibility = View.VISIBLE
        }
    }
}
