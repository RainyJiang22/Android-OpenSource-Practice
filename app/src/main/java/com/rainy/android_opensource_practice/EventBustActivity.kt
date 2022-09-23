package com.rainy.android_opensource_practice

import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.rainy.android_opensource_practice.databinding.ActivityEventBusBinding
import com.rainy.easybus.SubscribeEnv
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


        this.subscribeEvent(String::class.java) {
            Log.d("EasyBus", "this post data is $it")
            bind.testText.text = it
        }

        bind.btnPostBean.setOnClickListener {
            this.subscribeEvent(String::class.java) {
                Log.d("EasyBus", "this post data is $it")
                bind.testText.text = it
            }
        }

        bind.btnPostString.setOnClickListener {
            this.subscribeEvent(String::class.java) {
                Log.d("EasyBus", "this post data is $it")
                bind.testText.text = it
            }
        }
    }


}


data class HelloBean(val data: String)