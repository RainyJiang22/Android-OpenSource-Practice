package com.rainy.mvi.base

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */
class BaseData<T> {

    @SerializedName("errorCode")
    var code = -1

    @SerializedName("errorMsg")
    var msg: String? = null

    var data: T? = null

    var state: ReqState = ReqState.ERROR
}

enum class ReqState {
    SUCCESS, ERROR
}