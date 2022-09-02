package com.rainy.android_opensource_practice

import android.os.Bundle
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.rainy.android_opensource_practice.databinding.ActivityEventBusBinding
import com.sample.eventbus.api.SampleEventBus
import com.sample.eventbus_processor.Event

/**
 * @author jiangshiyu
 * @date 2022/9/2
 */
class EventBustActivity : BaseActivity() {

    override val bind by getBind<ActivityEventBusBinding>()

    private val eventTest = SampleEventBusTest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SampleEventBus.register(this)
        eventTest.register()

        bind.btnPostBean.setOnClickListener {
            SampleEventBus.post(HelloBean("hi"))
        }

        bind.btnPostString.setOnClickListener {
            SampleEventBus.post("Hello World")
        }
    }


}

class SampleEventBusTest {

    @Event
    fun stringFun(msg: String) {
        ToastUtils.showShort("$msg ${this.javaClass.simpleName}")
    }

    @Event
    fun benFun(msg: HelloBean) {
        ToastUtils.showShort("${msg.data} ${this.javaClass.simpleName}")
    }

    fun register() {
        SampleEventBus.register(this)
    }

    fun unregister() {
        SampleEventBus.unregister(this)
    }

}

data class HelloBean(val data: String)