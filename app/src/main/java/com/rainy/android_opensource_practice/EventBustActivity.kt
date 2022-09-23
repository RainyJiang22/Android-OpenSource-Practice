package com.rainy.android_opensource_practice

import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.rainy.android_opensource_practice.databinding.ActivityEventBusBinding
import com.rainy.easybus.SubscribeEnv
import com.rainy.easybus.extention.post
import com.rainy.easybus.extention.subscribeEvent
import com.sample.eventbus.api.SampleEventBus
import com.sample.eventbus_processor.Event

/**
 * @author jiangshiyu
 * @date 2022/9/2
 */
class EventBustActivity : BaseActivity() {

    override val bind by getBind<ActivityEventBusBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bind.btnPostBean.setOnClickListener {
            this.post(HelloBean("2 two bus by bean"))
        }

        bind.btnPostString.setOnClickListener {
            this.post("2 two bus by string")
        }

        this.subscribeEvent(String::class.java) {
            Log.d("EasyBus", "this post data is $it")
            bind.testText.text = it
        }

        this.subscribeEvent(HelloBean::class.java) {
            bind.testText.text = it.data
        }
    }


}


data class HelloBean(val data: String)