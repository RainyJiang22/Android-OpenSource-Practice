package com.rainy.ktretrofit

import com.rainy.ktretrofit.entity.ApiEmptyResponse
import com.rainy.ktretrofit.entity.ApiErrorResponse
import com.rainy.ktretrofit.entity.ApiFailedResponse
import com.rainy.ktretrofit.entity.ApiResponse
import com.rainy.ktretrofit.entity.ApiSuccessResponse

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */

fun <T> ApiResponse<T>.parseData(listenerBuilder: ResultBuilder<T>.() -> Unit) {
    val listener = ResultBuilder<T>().also(listenerBuilder)
    when (this) {
        is ApiSuccessResponse -> listener.onSuccess(this.response)
        is ApiEmptyResponse -> listener.onDataEmpty()
        is ApiFailedResponse -> listener.onFailed(this.errorCode, this.errorMsg)
        is ApiErrorResponse -> listener.onError(this.throwable)
    }
    listener.onComplete()
}

class ResultBuilder<T> {
    var onSuccess: (data: T?) -> Unit = {}
    var onDataEmpty: () -> Unit = {}
    var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, errorMsg ->
        errorMsg?.let {  }
    }
    var onError: (e: Throwable) -> Unit = { e ->
        e.message?.let {  }
    }
    var onComplete: () -> Unit = {}
}