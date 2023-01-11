package com.rainy.mvi.eventbus

/**
 * @author jiangshiyu
 * @date 2023/1/10
 */
sealed class Event {
    data class ShowInit(val msg: String) : Event()
}