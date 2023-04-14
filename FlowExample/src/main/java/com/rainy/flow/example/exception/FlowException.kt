package com.rainy.flow.example.exception

import android.util.Log
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

/**
 * @author jiangshiyu
 * @date 2023/4/14
 */
class FlowException {

    companion object {
        private const val TAG = "FlowException"
    }

    suspend fun takeNumber() {
        (1..5).asFlow().map {
            //当值为3得时候抛出异常
            check(it != 3) {
                "Value $it"
            }
            it * it
        }.onCompletion {
            Log.d(TAG, "onCompletion")
        }.collect {
            Log.d(TAG, "$it")
        }
    }

    suspend fun takeNumberByCatch() {
        (1..5).asFlow().map {
            check(it != 3) {
                "Value $it"
            }
            it * it
        }.onCompletion {
            Log.d(TAG, "onCompletion")
        }.catch { e ->
            Log.e(TAG, "Caught is $e")
        }.collect {
            Log.d(TAG, "$it")
        }
    }
}