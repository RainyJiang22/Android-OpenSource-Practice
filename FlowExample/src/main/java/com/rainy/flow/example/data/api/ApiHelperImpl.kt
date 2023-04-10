package com.rainy.flow.example.data.api

import com.rainy.flow.example.data.model.ApiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author jiangshiyu
 * @date 2023/4/10
 */
class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override fun getUsers(): Flow<List<ApiUser>> {
        return flow { emit(apiService.getUsers()) }
    }

    override fun getMoreUsers(): Flow<List<ApiUser>> {
        return flow { emit(apiService.getMoreUsers()) }
    }

    override fun getUsersWithError(): Flow<List<ApiUser>> {
        return flow { emit(apiService.getUserWithError()) }
    }
}