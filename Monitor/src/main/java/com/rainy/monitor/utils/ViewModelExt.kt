package com.rainy.monitor.utils

import android.R
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */

@MainThread
inline fun <reified VM : ViewModel> AppCompatActivity.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
}

inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val bind: (View) -> T = {
            T::class.java.getMethod("bind", View::class.java).invoke(null, it) as T
        }
        val getContentView: ComponentActivity.() -> View = {
            checkNotNull(findViewById<ViewGroup>(R.id.content).getChildAt(0)) {
                "Call setContentView or Use Activity's secondary constructor passing layout res id."
            }
        }
        bind(getContentView())
    }
}