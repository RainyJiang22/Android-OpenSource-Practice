package com.rainy.android_opensource_practice

import android.os.Bundle
import android.util.Log
import com.rainy.android_opensource_practice.databinding.ActivityEventBusBinding
import com.rainy.easybus.extention.postEvent
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/2
 */
class EventBustActivity : BaseActivity<BaseViewModel, ActivityEventBusBinding>() {


    override fun createObserver() {
        super.createObserver()

    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mViewBind.btnPostString.setOnClickListener {
            postEvent(HelloBean("this is message1"))
        }

        mViewBind.btnPostBean.setOnClickListener {
            postEvent(HelloBean("this is message2"))
        }
    }

}


data class HelloBean(val data: String)