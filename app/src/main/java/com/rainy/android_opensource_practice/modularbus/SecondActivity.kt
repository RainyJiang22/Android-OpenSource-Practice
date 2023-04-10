package com.rainy.android_opensource_practice.modularbus

import android.os.Bundle
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivitySecondBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2023/1/13
 */
class SecondActivity : BaseActivity<BaseViewModel, ActivitySecondBinding>() {
    override fun createObserver() {

//        lifecycleScope.launchWhenResumed {
//            EventDefineOfLoginEvents.login()
//                .observe(this@SecondActivity) { value: UserInfo? ->
//                    Log.d(EventBustTestActivity.TAG, "createObserver: $value")
//                    toast("$value")
//                }
//        }
//
//        SingleEvent.observer(this) {
//            toast("$it")
//        }

    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {


        mViewBind?.send?.setOnClickListener {
//            EventDefineOfLoginEvents.login().post(UserInfo("RainyJiang"))

        }
    }

    override fun showLoading(message: String) {
    }

    override fun onBundle(bundle: Bundle) {

    }
}