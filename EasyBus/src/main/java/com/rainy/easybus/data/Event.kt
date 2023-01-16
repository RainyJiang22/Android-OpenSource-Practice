package com.rainy.easybus.data

/**
 * @author jiangshiyu
 * @date 2023/1/16
 */
sealed class Event {

    data class show(val data: Any) : Event()
}