package com.rainy.modular.eventbus

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
object SingleEvent {


    val flowEvent = MutableSharedFlow<Any>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    fun post(event: Any) {
        flowEvent.tryEmit(event)
    }

    fun observer(
        lifecycleOwner: LifecycleOwner,
        onChange: suspend (any: Any) -> Unit,
    ) = lifecycleOwner.mainDispatcher {

        flowEvent.debounce(300)
            .collectLatest {
                onChange.invoke(it)
            }
    }
}

