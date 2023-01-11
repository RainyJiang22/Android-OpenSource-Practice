package com.rainy.mvi.model.repository

import com.rainy.mvi.base.BaseData
import com.rainy.mvi.base.BaseRepository
import com.rainy.mvi.model.bean.Article
import com.rainy.mvi.model.bean.Banner
import com.rainy.mvi.model.http.WanRetrofitClient
import com.rainy.mvi.model.http.api.WanApi

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
class HomeRepository : BaseRepository() {

    private val service = WanRetrofitClient.getService(WanApi::class.java)


    //请求banner数据
    suspend fun requestBannerData(): BaseData<List<Banner>> {
        return executeRequest { service.getBanner() }
    }

    suspend fun requestHomeData(page: Int): BaseData<Article> {
        return executeRequest { service.getArticle(page) }
    }

}