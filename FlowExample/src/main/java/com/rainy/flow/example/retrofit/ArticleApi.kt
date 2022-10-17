package com.rainy.flow.example.retrofit

import com.rainy.flow.example.retrofit.data.ApiResponse
import com.rainy.flow.example.retrofit.data.BannerResponse
import retrofit2.http.GET

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
interface ArticleApi {

    //获取banner数据
    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerResponse>>

}