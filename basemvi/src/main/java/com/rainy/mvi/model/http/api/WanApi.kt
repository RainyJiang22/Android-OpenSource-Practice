package com.rainy.mvi.model.http.api

import com.rainy.mvi.base.BaseData
import com.rainy.mvi.model.bean.Article
import com.rainy.mvi.model.bean.Banner
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
interface WanApi {

    @GET("banner/json")
    suspend fun getBanner(): BaseData<List<Banner>>

    //页码从0开始
    @GET("article/list/{page}/json")
    suspend fun getArticle(@Path("page") page: Int): BaseData<Article>

}