package com.rainy.ktretrofit.base

import com.rainy.ktretrofit.BuildConfig
import com.rainy.ktretrofit.entity.ApiEmptyResponse
import com.rainy.ktretrofit.entity.ApiErrorResponse
import com.rainy.ktretrofit.entity.ApiFailedResponse
import com.rainy.ktretrofit.entity.ApiResponse
import com.rainy.ktretrofit.entity.ApiSuccessResponse

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
class BaseRepository {
    suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        kotlin.runCatching {
            block.invoke()
        }.onSuccess { data: ApiResponse<T> ->
            handleHttpOk(data)
        }.onFailure {
            handleHttpError<T>(it)
        }
        return ApiEmptyResponse()
    }

    /**
     * 非后台返回错误,捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable): ApiErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        return ApiErrorResponse(e)
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
        return if (data.isSuccess) {
            getHttpSuccessResponse(data)
        } else {
            ApiFailedResponse(data.errorCode, data.errorMsg)
        }
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
        val data = response.data
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            ApiEmptyResponse()
        } else {
            ApiSuccessResponse(data)
        }
    }

}