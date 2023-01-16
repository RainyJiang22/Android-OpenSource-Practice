package com.rainy.easybus.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.rainy.easybus.extention.launchWhenStateAtLeast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/9/26
 */
class EasyBusCore : ViewModel() {


    //正常事件
    private val eventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    //粘性事件
    private val stickyEventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()


    private fun getEventFlow(
        eventName: String,
        isSticky: Boolean,
    ): MutableSharedFlow<Any> {
        return if (isSticky) {
            stickyEventFlows[eventName]
        } else {
            eventFlows[eventName]
        } ?: MutableSharedFlow<Any>(
            replay = if (isSticky) 1 else 0,
            extraBufferCapacity = Int.MAX_VALUE
        ).also {
            val sharedFlow = it.asSharedFlow().shareIn(CoroutineScope(Dispatchers.Main),
                started = SharingStarted.WhileSubscribed())
            if (isSticky) {
                stickyEventFlows[eventName] = it
            } else {
                eventFlows[eventName] = it
            }
        }
    }

    fun <T : Any> observeEvent(
        lifecycleOwner: LifecycleOwner,
        eventName: String,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher,
        isSticky: Boolean,
        onReceived: (T) -> Unit,
    ): Job {
        return lifecycleOwner.launchWhenStateAtLeast(minState) {
            getEventFlow(eventName, isSticky).collect { value ->
                this.launch(dispatcher) {
                    invokeReceived(value, onReceived)
                }
            }
        }
    }


    suspend fun <T : Any> observeWithoutLifecycle(
        eventName: String,
        isSticky: Boolean,
        onReceived: (T) -> Unit,
    ) {
        getEventFlow(eventName, isSticky).collect { value ->
            invokeReceived(value, onReceived)
        }
    }


    fun postEvent(eventName: String, value: Any, timeMillis: Long) {
        listOfNotNull(
            getEventFlow(eventName, false),
            getEventFlow(eventName, true)
        ).forEach { flow ->
            viewModelScope.launch {
                delay(timeMillis)
                flow.emit(value)
            }
        }
    }


    fun removeStickyEvent(eventName: String) {
        stickyEventFlows.remove(eventName)
    }

    fun clearStickyEvent(eventName: String) {
        stickyEventFlows[eventName]?.resetReplayCache()
    }


    private fun <T : Any> invokeReceived(value: Any, onReceived: (T) -> Unit) {
        try {
            onReceived.invoke(value as T)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}