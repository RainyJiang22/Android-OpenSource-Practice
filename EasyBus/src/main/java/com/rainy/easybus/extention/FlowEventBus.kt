package com.rainy.easybus.extention

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import com.rainy.easybus.data.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

/**
 * @author jiangshiyu
 * @date 2023/1/16
 */
object FlowEventBus {

    // 定义事件Event
    // 发送者Publisher 如何发送事件
    // 如何存储Event并且分发
    // 如何订阅事件


    private val flowEvents = ConcurrentHashMap<String, MutableSharedFlow<Event>>()


    fun getFlow(key: String): MutableSharedFlow<Event> {
        return flowEvents[key] ?: MutableSharedFlow<Event>().also {
            flow<Event> {
                it.shareIn(MainScope(), started = SharingStarted.WhileSubscribed())
            }
        }
    }


    //发送事件
    fun post(event: Event, delay: Long = 0L) {
        MainScope().launch {
            delay(delay)
            getFlow(event.javaClass.simpleName).emit(event)
        }
    }

    //订阅事件
    inline fun <reified T : Event> observe(
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        crossinline onReceive: (T) -> Unit,
    ) = lifecycleOwner.lifecycleScope.launch(dispatcher) {

        getFlow(T::class.java.simpleName).collect {
            lifecycleOwner.lifecycle.whenStateAtLeast(minState) {
                if (it is T) onReceive.invoke(it)
            }
        }
    }


}