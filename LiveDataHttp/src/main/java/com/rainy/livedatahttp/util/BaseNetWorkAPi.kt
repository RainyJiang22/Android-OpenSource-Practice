package com.rainy.livedatahttp.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */
abstract class BaseNetWorkAPi {


    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }


    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder


    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder


    private val okHttpClient: OkHttpClient
        get() {
            var builder = OkHttpClient.Builder()
            builder = setHttpClientBuilder(builder)
            return builder.build()
        }

}