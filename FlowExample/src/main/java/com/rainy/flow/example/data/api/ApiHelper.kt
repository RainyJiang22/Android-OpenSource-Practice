package com.rainy.flow.example.data.api

import com.rainy.flow.example.data.model.ApiUser
import kotlinx.coroutines.flow.Flow

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
interface ApiHelper {

    fun getUsers(): Flow<List<ApiUser>>

    fun getMoreUsers(): Flow<List<ApiUser>>

    fun getUsersWithError(): Flow<List<ApiUser>>
}