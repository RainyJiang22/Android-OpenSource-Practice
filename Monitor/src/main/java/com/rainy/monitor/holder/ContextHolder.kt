package com.rainy.monitor.holder

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
internal object ContextHolder {


    lateinit var context: Application
        private set

    fun init(context: Application) {
        this.context = context
    }

}