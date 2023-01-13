package com.rainy.android_opensource_practice.modularbus

import android.os.Bundle
import android.util.Log
import com.rainy.android_opensource_practice.databinding.ActivityModularBusBinding
import com.rainy.modular.eventbus.LiveDataBus
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
class EventBustTestActivity : BaseVmVbActivity<BaseViewModel, ActivityModularBusBinding>() {

    companion object {
        const val TAG = "EventBus"
    }

    override fun createObserver() {
        LiveDataBus.getChannel("key_test", String::class.java).observe(this) {
            Log.d(TAG, "createObserver: $it")
        }
    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.send.setOnClickListener {
            LiveDataBus.getChannel("key_test").setValue("Hello RainyJiang")
        }

    }

    override fun showLoading(message: String) {

    }
}