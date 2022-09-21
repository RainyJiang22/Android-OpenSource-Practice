package com.rainy.glidektx

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author jiangshiyu
 * @date 2022/9/21
 */
class ProgressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)

        //将progressResponseBody作为代理
        val url = request.url().toString()
        return originalResponse.newBuilder().body(
            ProgressResponseBody(url,originalResponse.body())
        ).build()
    }
}