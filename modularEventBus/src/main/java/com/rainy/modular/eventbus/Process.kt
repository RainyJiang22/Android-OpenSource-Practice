package com.rainy.modular.eventbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
fun LifecycleOwner.startCoroutineScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    context: CoroutineContext = EmptyCoroutineContext,
    onThrowable: ((context: CoroutineContext, throwable: Throwable?) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val supervisorJob = SupervisorJob(context[Job])
    val scope =
        CoroutineScope(dispatcher + context + supervisorJob + CoroutineExceptionHandler { coroutineContext, throwable ->
            onThrowable?.invoke(coroutineContext, throwable)
        })
    val job = scope.launch {
        block.invoke(this)
    }
    LifecycleController(lifecycle, job)
    return job
}

class LifecycleController(
    private val lifecycle: Lifecycle,
    parentJob: Job
) {

    private val observer = LifecycleEventObserver { source, _ ->
        if (source.lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            finish()
            parentJob.cancel()
        }
    }

    init {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            lifecycle.addObserver(observer)
        } else {
            parentJob.cancel()
        }
    }

    private fun finish() {
        lifecycle.removeObserver(observer)
    }
}


fun LifecycleOwner.mainDispatcher(
    onThrowable: ((context: CoroutineContext, throwable: Throwable?) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
) =
    startCoroutineScope(onThrowable = onThrowable, block = block)
