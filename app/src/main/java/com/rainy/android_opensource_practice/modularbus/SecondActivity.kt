package com.rainy.android_opensource_practice.modularbus

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.pengxr.modular.eventbus.generated.events.EventDefineOfLoginEvents
import com.rainy.android_opensource_practice.databinding.ActivitySecondBinding
import com.rainy.android_opensource_practice.toast
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
class SecondActivity : BaseVmVbActivity<BaseViewModel, ActivitySecondBinding>() {
    override fun createObserver() {

        lifecycleScope.launchWhenResumed {
            EventDefineOfLoginEvents.login()
                .observe(this@SecondActivity) { value: UserInfo? ->
                    Log.d(EventBustTestActivity.TAG, "createObserver: $value")
                    toast("$value")
                }
        }

    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {


        mViewBind.send.setOnClickListener {
//            EventDefineOfLoginEvents.login().post(UserInfo("RainyJiang"))

        }
    }

    override fun showLoading(message: String) {
    }
}