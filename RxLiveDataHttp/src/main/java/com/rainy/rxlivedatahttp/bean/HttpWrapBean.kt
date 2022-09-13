package com.rainy.rxlivedatahttp.bean

import com.rainy.rxlivedatahttp.exception.BaseHttpException
import com.rainy.rxlivedatahttp.exception.ServerCodeNoSuccessException

/**
 * @author jiangshiyu
 * @date 2022/9/9
 */
data class HttpWrapBean<T>(val status: Int, val msg: String, val data: T) {

    companion object {
        fun error(throwable: ServerCodeNoSuccessException):HttpWrapBean<*> {
            val exception = BaseHttpException.generateException(throwable)
            return HttpWrapBean(exception.errorCode, exception.errorMessage, null)
        }
    }

    val isSuccess: Boolean
        get() = status == 200

}