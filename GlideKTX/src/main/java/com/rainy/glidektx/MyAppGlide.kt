package com.rainy.glidektx

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * @author jiangshiyu
 * @date 2022/9/15
 */


/**
 * 在大多数情况下 Glide 的默认配置就已经能够满足我们的需求了，像缓存池大小，磁盘缓存策略等都不需要我们主动去设置，但 Glide 也提供了 AppGlideModule 让开发者可以去实现自定义配置。
 * 对于一个 App 来说，在加载图片的时候一般都是使用同一张 placeholder，如果每次加载图片时都需要来手动设置一遍的话就显得很多余了，
 * 此时就可以通过 AppGlideModule 来设置默认的 placeholder

首先需要继承于 AppGlideModule，在 `applyOptions`方法中设置配置参数，然后为实现类添加 @GlideModule 注解，
这样在编译阶段 Glide 就可以通过 APT 解析到我们的这一个实现类，然后将我们的配置参数设置为默认值
 */
@GlideModule
class MyAppGlide : AppGlideModule() {

    //控制是否从Manifest中文件配置解析
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDiskCache(
            //配置磁盘缓存目录和最大缓存
            DiskLruCacheFactory(
                (context.externalCacheDir ?: context.cacheDir).absolutePath,
                "imageCache",
                1024 * 1024 * 50
            )
        )
        builder.setDefaultRequestOptions {
            return@setDefaultRequestOptions RequestOptions()
                .placeholder(android.R.drawable.ic_menu_upload_you_tube)
                .error(android.R.drawable.ic_menu_call)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .format(DecodeFormat.DEFAULT)
                .encodeQuality(90)
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //注册okhttpUrlLoader
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(OkHttpClient())
        )
    }
}