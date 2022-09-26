package com.rainy.easybus

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */
object EventBusInitializer {

    lateinit var application: Application

    //初始化
    fun init(application: Application) {
        EventBusInitializer.application = application
    }
}