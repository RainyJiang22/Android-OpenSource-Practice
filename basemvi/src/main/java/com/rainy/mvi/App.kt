package com.rainy.mvi

import android.app.Application
import com.rainy.mvi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author jiangshiyu
 * @date 2023/1/11
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            //启动koin
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}