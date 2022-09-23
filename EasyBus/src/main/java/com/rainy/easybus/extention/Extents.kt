package com.rainy.easybus.extention

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.rainy.easybus.LifecycleJob
import com.rainy.easybus.MessageCenter
import com.rainy.easybus.SubscribeEnv
import kotlinx.coroutines.Job

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