package com.rainy.modular.eventbus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
object LiveDataBus {

    val bus: MutableMap<String, BusMutableLiveData<Any>> by lazy { HashMap() }


    fun <T : Any> with(target: String, type: Class<T>): BusMutableLiveData<Any>? {
        if (!bus.containsKey(target)) {
            bus[target] = BusMutableLiveData()
        }
        return bus[target]
    }


    //这个订阅过程中发现，订阅者会收到订阅之前的事件，这对于一个事件总线框架来说是不可取的
    fun with(target: String): BusMutableLiveData<Any>? {
        return with(target, Any::class.java)
    }



}