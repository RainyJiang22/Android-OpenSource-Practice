package com.rainy.android_opensource_practice.share

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
//本地事件总线
object LocalEventBus {

    val events = MutableSharedFlow<Event>()

    suspend fun postEvent(event: Event) {
        events.emit(event)
    }
}

data class Event(val timestamp: Long)