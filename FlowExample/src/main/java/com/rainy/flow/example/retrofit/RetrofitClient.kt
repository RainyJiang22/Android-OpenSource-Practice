package com.rainy.flow.example.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
object RetrofitClient {

    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl("https://wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val articleApi: ArticleApi by lazy {
        instance.create(ArticleApi::class.java)
    }
}