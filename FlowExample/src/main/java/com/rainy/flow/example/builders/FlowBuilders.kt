package com.rainy.flow.example.builders

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * @author jiangshiyu
 * @date 2023/4/14
 */
class FlowBuilders {



    suspend fun flowBuilders() {

        flowOf(4, 2, 3, 45, 5)
            .collect {
                print(it)
            }

        (1..5).asFlow()
            .collect {
                print(it)
            }

        flow {
            (0..10).forEach {
                emit(it)
            }
        }.collect {
            print(it)
        }


        @OptIn(ExperimentalCoroutinesApi::class)
        channelFlow {
            (0..10).forEach {
                send(it)
            }
        }.collect {
            print(it)
        }
    }
}