package com.rainy.easybus

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */
object EventBusInitializer {

    lateinit var application: Application

    fun init(application: Application) {
        EventBusInitializer.application = application
    }
}