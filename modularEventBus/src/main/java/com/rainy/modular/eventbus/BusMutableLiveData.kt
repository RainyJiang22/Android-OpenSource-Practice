package com.rainy.modular.eventbus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
open class BusMutableLiveData<T> : MutableLiveData<T?>() {
    private val observerMap: MutableMap<Observer<*>, Observer<*>> = HashMap()


    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        super.observe(owner, observer)
        kotlin.runCatching {
            hook(observer)
        }.onFailure {
            it.printStackTrace()
        }
    }


    override fun observeForever(observer: Observer<in T?>) {
        if (!observerMap.containsKey(observer)) {
            observerMap[observer] = ObserverWrapper(observer)
        }
        super.observeForever(observer)
    }

    override fun removeObserver(observer: Observer<in T?>) {
        var realObserver: Observer<*>? = null
        realObserver = if (observerMap.containsKey(observer)) {
            observerMap.remove(observer)
        } else {
            observer
        }
        super.removeObserver(realObserver as Observer<in T?>)
    }


    @Throws(Exception::class)
    private fun hook(observer: Observer<in T?>?) {
        //get wrapper's version
        val classLiveData = LiveData::class.java
        val fieldObservers = classLiveData.getDeclaredField("mObservers")
        fieldObservers.isAccessible = true
        val objectObservers = fieldObservers[this]
        val classObservers: Class<*> = objectObservers.javaClass
        val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
        methodGet.isAccessible = true
        val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        if (objectWrapper == null) {
            throw NullPointerException("Wrapper can not be bull!")
        }
        val classObserverWrapper: Class<*> = objectWrapper.javaClass.superclass as Class<*>
        val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
        fieldLastVersion.isAccessible = true
        //get livedata's version
        val fieldVersion = classLiveData.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        val objectVersion = fieldVersion[this]
        //set wrapper's version
        fieldLastVersion[objectWrapper] = objectVersion
    }
}