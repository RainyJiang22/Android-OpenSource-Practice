package com.rainy.rxlivedatahttp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
object LiveDataManager {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://getman.cn/mock/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
        .build()

    val apiService = retrofit.create(ApiInterface::class.java)

}