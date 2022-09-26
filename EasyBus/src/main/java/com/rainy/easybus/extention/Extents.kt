package com.rainy.easybus.extention

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import com.rainy.easybus.LifecycleJob
import com.rainy.easybus.MessageCenter
import com.rainy.easybus.SubscribeEnv
import com.rainy.easybus.core.EasyBusCore
import com.rainy.easybus.scope.AppViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */

inline fun <reified T> LifecycleOwner.subscribeEvent(
    event: Class<T>,
    env: SubscribeEnv = SubscribeEnv.MAIN,
    crossinline dos: (T) -> Unit
) = MessageCenter.onEvent(event, dos, this, env)


inline fun <reified T> LifecycleOwner.post(
    event: T,
    isStick: Boolean = false
) = MessageCenter.post(event, isStick)

fun Job.setLifeCycle(owner: Lifecycle) {
    val proxyJob = LifecycleJob(this)
    owner.addObserver(proxyJob)
}

fun <T> LifecycleOwner.launchWhenStateAtLeast(
    minState: Lifecycle.State,
    block: suspend CoroutineScope.() -> T
): Job {
    return lifecycleScope.launch {
        lifecycle.whenStateAtLeast(minState, block)
    }
}


/**
 * App Scope事件
 */
@MainThread
inline fun <reified T> LifecycleOwner.observeEvent(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
): Job {
    return AppViewModelProvider.getApplicationScopeViewModel(EasyBusCore::class.java)
        .observeEvent(
            this,
            T::class.java.name,
            minActiveState,
            dispatcher,
            isSticky,
            onReceived
        )

}

//Application范围的事件
inline fun <reified T> postEvent(event: T, timeMillis: Long = 0L) {
    AppViewModelProvider.getApplicationScopeViewModel(EasyBusCore::class.java)
        .postEvent(T::class.java.name, event!!, timeMillis)
}

@MainThread
inline fun <reified T> observeEvent(
    coroutineScope: CoroutineScope,
    isSticky: Boolean = false,
    noinline onReceived: (T) -> Unit
): Job {
    return coroutineScope.launch {
        AppViewModelProvider.getApplicationScopeViewModel(EasyBusCore::class.java)
            .observeWithoutLifecycle(
                T::class.java.name,
                isSticky,
                onReceived
            )
    }
}