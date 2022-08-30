package com.sample.eventbus_processor

/**
 * @author jiangshiyu
 * @date 2022/8/26
 */
data class SubscriberInfo(
    val subscriberClass: Class<*>,
    val methodList: List<EventMethodInfo>
)
