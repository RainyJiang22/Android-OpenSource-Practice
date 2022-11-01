package com.rainy.easy.datastore

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/11/1
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}