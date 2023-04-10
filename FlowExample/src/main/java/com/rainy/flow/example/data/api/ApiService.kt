package com.rainy.flow.example.data.api

import com.rainy.flow.example.data.model.ApiUser
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
interface ApiService {


    @GET("users")
    suspend fun getUsers(): List<ApiUser>

    @GET("more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("error")
    suspend fun getUserWithError(): List<ApiUser>
}