package com.sample.eventbus.api

import com.sample.eventbus_processor.SubscriberInfo

/**
 * @author jiangshiyu
 * @date 2022/8/26
 */
object SampleEventBus {

    private val subscriptions = mutableSetOf<Any>()

    private const val PACKAGE_NAME = "github.leavesc.easyeventbus"

    private const val CLASS_NAME = "EventBusInject"

    private const val CLASS_PATH = "$PACKAGE_NAME.$CLASS_NAME"

    private val clazz = Class.forName(CLASS_PATH)

    //通过反射生成 EventBusInject 对象
    private val instance = clazz.newInstance()

    @Synchronized
    fun register(subscriber: Any) {
        subscriptions.add(subscriber)
    }

    @Synchronized
    fun unregister(subscriber: Any) {
        subscriptions.remove(subscriber)
    }

    @Synchronized
    fun post(event: Any) {
        subscriptions.forEach { subscriber ->
            val subscriberInfo = getSubscriberInfo(subscriber.javaClass)
            if (subscriberInfo != null) {
                val methodList = subscriberInfo.methodList
                methodList.forEach { method ->
                    if (method.eventType == event.javaClass) {
                        val declaredMethod = subscriber.javaClass.getDeclaredMethod(
                            method.methodName, method.eventType
                        )
                        declaredMethod.invoke(subscriber, event)
                    }
                }
            }
        }
    }


    //通过反射调用EvenInject的getSubscriberInfo方法
    private fun getSubscriberInfo(subscriberClass: Class<*>): SubscriberInfo? {
        val method = clazz.getMethod("getSubscriberInfo", Class::class.java)
        return method.invoke(instance, subscriberClass) as? SubscriberInfo
    }
}