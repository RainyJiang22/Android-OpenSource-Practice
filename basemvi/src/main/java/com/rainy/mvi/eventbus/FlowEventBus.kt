package com.rainy.mvi.eventbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */
object FlowEventBus {

    //hashMap存储SharedFlow
    private val flowEvents = ConcurrentHashMap<String, MutableSharedFlow<Event>>()

    //获取flow
    fun getFlow(key: String): MutableSharedFlow<Event> {
        return flowEvents[key] ?: MutableSharedFlow<Event>().also { flowEvents[key] = it }
    }


    //发送
    fun post(event: Event, time: Long = 0) {
        MainScope().launch {
            delay(time)
            getFlow(event.javaClass.simpleName).emit(event)
        }
    }

    //订阅
    inline fun <reified T : Event> observe(
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        crossinline onReceive: (T) -> Unit,
    ) = lifecycleOwner.lifecycleScope.launch(dispatcher) {
        getFlow(T::class.java.simpleName).collect {
            lifecycleOwner.lifecycle.whenStateAtLeast(minState) {
                if (it is T) onReceive(it)
            }
        }
    }
}