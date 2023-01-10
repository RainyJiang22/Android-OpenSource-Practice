package com.rainy.mvi.base

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */
open class BaseRepository {

    suspend fun <T : Any> executeRequest(block: suspend () -> BaseData<T>): BaseData<T> {
        val baseData = block.invoke()
        if (baseData.code == 0) {
            baseData.state = ReqState.SUCCESS
        } else {
            baseData.state = ReqState.ERROR
        }
        return baseData
    }
}