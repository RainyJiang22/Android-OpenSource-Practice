package com.rainy.modular.eventbus

import androidx.lifecycle.Observer



/**
 * 可以写一个ObserverWrapper，把真正的回调给包装起来。把ObserverWrapper传给observeForever，
 * 那么在回调的时候我们去检查调用栈，如果回调是observeForever方法引起的，那么就不回调真正的订阅者
 */
class ObserverWrapper<T>(private val observer: Observer<in T>?) : Observer<T> {
    override fun onChanged(t: T?) {
        if (observer != null) {
            if (isCallOnObserve) {
                return
            }
            observer.onChanged(t)
        }
    }

    private val isCallOnObserve: Boolean
        get() {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace.isNotEmpty()) {
                for (element in stackTrace) {
                    if ("android.arch.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
                        return true
                    }
                }
            }
            return false
        }

}