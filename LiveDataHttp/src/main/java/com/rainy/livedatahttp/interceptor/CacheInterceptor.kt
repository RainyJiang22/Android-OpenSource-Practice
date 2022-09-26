package com.rainy.livedatahttp.interceptor

import android.app.Application
import com.rainy.livedatahttp.util.NetWorkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author jiangshiyu
 * @date 2022/9/23
 *
 * 缓存拦截器(默认缓存7天)
 */
val appContext: Application = Application()

class CacheInterceptor(var day: Int = 7) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetWorkUtil.isNetworkAvailable(appContext)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val response = chain.proceed(request)
        if (!NetWorkUtil.isNetworkAvailable(appContext)) {
            val maxAge = 60 * 60
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * day // tolerate 4-weeks stale
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
        return response
    }
}