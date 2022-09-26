package com.rainy.android_opensource_practice

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.rainy.easybus.EventBusInitializer

/**
 * @author jiangshiyu
 * @date 2022/9/26
 */

val eventViewModel: EventViewModel by lazy { BaseApp.eventViewModelInstance }

class BaseApp : Application(), ViewModelStoreOwner {


    private var mFactory: ViewModelProvider.Factory? = null
    private lateinit var mAppViewModelStore: ViewModelStore


    companion object {
        lateinit var eventViewModelInstance: EventViewModel
    }


    override fun onCreate() {
        super.onCreate()
        EventBusInitializer.init(this)
        mAppViewModelStore = ViewModelStore()
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }
}