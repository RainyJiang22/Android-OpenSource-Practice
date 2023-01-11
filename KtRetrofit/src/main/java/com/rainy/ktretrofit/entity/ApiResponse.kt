package com.rainy.ktretrofit.entity

import java.io.Serializable

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
open class ApiResponse<T>(
    open val data: T? = null,
    open val errorCode: Int? = null,
    open val errorMsg: String? = null,
    open val error: Throwable? = null,
) : Serializable {

    val isSuccess: Boolean
        get() = errorCode == 0


    override fun toString(): String {
        return "ApiResponse(data=$data, errorCode=$errorCode, errorMsg=$errorMsg, error=$error)"
    }
}


//成功拿到数据
data class ApiSuccessResponse<T>(val response: T) : ApiResponse<T>(data = response)

//数据为空
class ApiEmptyResponse<T> : ApiResponse<T>()

//数据拿取失败
data class ApiFailedResponse<T>(override val errorCode: Int?, override val errorMsg: String?) :
    ApiResponse<T>(errorCode = errorCode, errorMsg = errorMsg)

//错误
data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>(error = throwable)