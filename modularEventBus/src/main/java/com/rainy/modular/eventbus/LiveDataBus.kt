package com.rainy.modular.eventbus

import androidx.lifecycle.MutableLiveData

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
object LiveDataBus {

    val bus: MutableMap<String, MutableLiveData<Any>> by lazy { HashMap() }


    fun <T : Any> getChannel(target: String, type: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(target)) {
            bus[target] = MutableLiveData()

        }
        return bus[target] as MutableLiveData<T>
    }


    //这个订阅过程中发现，订阅者会收到订阅之前的事件，这对于一个事件总线框架来说是不可取的
    fun getChannel(target: String): MutableLiveData<Any> {
        return getChannel(target, Any::class.java)
    }

    /**
     * 可以写一个ObserverWrapper，把真正的回调给包装起来。把ObserverWrapper传给observeForever，
     * 那么在回调的时候我们去检查调用栈，如果回调是observeForever方法引起的，那么就不回调真正的订阅者
     */



}