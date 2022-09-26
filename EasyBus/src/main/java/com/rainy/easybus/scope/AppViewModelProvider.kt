package com.rainy.easybus.scope

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.rainy.easybus.EventBusInitializer

/**
 * @author jiangshiyu
 * @date 2022/9/26
 */
object AppViewModelProvider : ViewModelStoreOwner {

    private val eventViewModelStore: ViewModelStore = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore {
        return eventViewModelStore
    }


    private val mApplicationProvider: ViewModelProvider by lazy {
        ViewModelProvider(
            AppViewModelProvider,
            ViewModelProvider.AndroidViewModelFactory.getInstance(EventBusInitializer.application)
        )
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        return mApplicationProvider[modelClass]
    }
}