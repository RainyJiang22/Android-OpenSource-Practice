package com.rainy.easybus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */
class LifecycleJob(private val job: Job) : Job by job, LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.cancel()
        }
    }

    override fun cancel(cause: CancellationException?) {
        if (!job.isCancelled) {
            job.cancel()
        }
    }
}