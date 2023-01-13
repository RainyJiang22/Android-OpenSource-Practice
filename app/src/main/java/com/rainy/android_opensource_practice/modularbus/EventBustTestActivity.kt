package com.rainy.android_opensource_practice.modularbus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.pengxr.modular.eventbus.generated.events.EventDefineOfLoginEvents
import com.rainy.android_opensource_practice.databinding.ActivityModularBusBinding
import com.rainy.modular.eventbus.LiveDataBus
import com.rainy.modular.eventbus.SingleEvent
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
        LiveDataBus.with("key_test", String::class.java)?.observe(this) {
            Log.d(TAG, "createObserver: $it")
        }
//        lifecycleScope.launchWhenResumed {
//            EventDefineOfLoginEvents.login()
//                .observe(this@EventBustTestActivity) { value: UserInfo? ->
//                    Log.d(TAG, "createObserver: $value")
//                    toast("$value")
//                }
//        }
    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.send.setOnClickListener {
            SingleEvent.post(UserInfo("RainyJiang"))
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    override fun showLoading(message: String) {

    }
}