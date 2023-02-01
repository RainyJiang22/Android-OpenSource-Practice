package com.rainy.easybus

import android.security.identity.DocTypeNotSupportedException
import androidx.lifecycle.LifecycleOwner
import com.rainy.easybus.extention.setLifeCycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */
object MessageCenter {

    var events = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()
        private set

    var stickyEvents = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()
        private set


    inline fun <reified T> post(event: T, isStick: Boolean) {
        val cls = T::class.java
        if (!isStick) {
            events.getOrElse(cls) {
                MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
            }.tryEmit(event as Any)
        } else {
            stickyEvents.getOrElse(cls) {
                MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
            }.tryEmit(stickyEvents)
        }

    }

    inline fun <reified T> onEvent(
        event: Class<T>,
        crossinline dos: (T & Any) -> Unit,
        owner: LifecycleOwner,
        env: SubscribeEnv
    ) {
        initEvent(event)
        val coroutineScope: CoroutineScope = when (env) {
            SubscribeEnv.IO -> {
                CoroutineScope(Dispatchers.IO)
            }
            SubscribeEnv.DEFAULT -> {
                CoroutineScope(Dispatchers.Default)
            }
            else -> {
                CoroutineScope(Dispatchers.Main)
            }
        }

        coroutineScope.launch {
            events[event]?.collect {
                if (it is T) {
                    dos.invoke(it)
                }
            }
        }.setLifeCycle(owner.lifecycle)

        coroutineScope.launch {
            stickyEvents[event]?.collect {
                if (it is T) {
                    dos.invoke(it)
                }
            }
        }.setLifeCycle(owner.lifecycle)

    }

    fun <T> initEvent(event: Class<T>) {
        if (!events.containsKey(event)) {
            events[event] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }

        if (!stickyEvents.containsKey(event)) {
            stickyEvents[event] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
    }
}