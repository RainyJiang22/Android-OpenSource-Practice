package com.rainy.ktretrofit.base


import com.rainy.ktretrofit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
abstract class BaseRetrofitClient {

    companion object CLIENT {
        private const val TIME_OUT = 30L
    }


    private val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(getHttpLoggingInterceptor())
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        handleBuilder(builder)
        builder.build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        return logging
    }

    abstract fun handleBuilder(builder: OkHttpClient.Builder)

    fun <T> getService(serviceClass: Class<T>, baseUrl: String): T {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .build()
            .create(serviceClass)
    }
}