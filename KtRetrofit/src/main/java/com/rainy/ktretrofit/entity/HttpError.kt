package com.rainy.ktretrofit.entity

import android.util.Log
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
enum class HttpError(var code: Int, var errorMsg: String) {
    TOKEN_EXPIRE(3001, "token is expired"),
    PARAMS_ERROR(4003, "params is error")
    // ...... more
}

internal fun handlingApiExceptions(code: Int?, errorMsg: String?) = when (code) {
    HttpError.TOKEN_EXPIRE.code -> Log.e("Error", HttpError.TOKEN_EXPIRE.errorMsg)
    HttpError.PARAMS_ERROR.code -> Log.e("Error", HttpError.PARAMS_ERROR.errorMsg)
    else -> errorMsg?.let { Log.e("Error", it) }
}

internal fun handlingExceptions(e: Throwable) = when (e) {
    is HttpException -> {
    }
    is CancellationException -> {
    }
    is SocketTimeoutException -> {
    }
    is JsonParseException -> {
    }
    else -> {
    }
}
