package com.rainy.livedatahttp.util

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 服务器返回的基类
 */
abstract class BaseResponse<T> {

    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): String

    abstract fun getResponseMsg(): String
}