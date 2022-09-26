package com.rainy.android_opensource_practice

import android.app.Application
import com.rainy.easybus.EventBusInitializer

/**
 * @author jiangshiyu
 * @date 2022/9/26
 */


class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        EventBusInitializer.init(this)
    }

}