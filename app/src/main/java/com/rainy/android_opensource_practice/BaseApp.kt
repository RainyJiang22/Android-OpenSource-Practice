package com.rainy.android_opensource_practice

import android.app.Application
import android.content.Context
import com.rainy.android_opensource_practice.user.AppDatabase
import com.rainy.easy.datastore.BaseApplication
import com.rainy.easybus.EventBusInitializer

/**
 * @author jiangshiyu
 * @date 2022/9/26
 */


class BaseApp : BaseApplication() {


    override fun onCreate() {
        super.onCreate()
        sContext = this
        EventBusInitializer.init(this)
    }


    companion object {

        private var sContext: Context? = null

        @JvmStatic
        fun getContext(): Context? {
            return sContext
        }
    }
}